# PRD 通用 Markdown 格式提取脚本
# 用途：从 Markdown 文档中提取干净的纯文字骨架，保留所有文字语义，丢弃格式噪声
# 适用：任何 Markdown 文档（输入文档无格式特征假设）
#
# 用法示例：
#   & 'format-convert.ps1' `
#       -InputFile  'C:\path\to\source.md' `
#       -OutputFile 'C:\path\to\PRD-01-01-项目管理.md' `
#       -StartLine  118 `
#       -EndLine    1400 `
#       -L2Title    '项目管理'
#
# 处理规则：
#   保留：# / ## / ### 标题、有序/无序列表、纯文字、加粗(**)
#   保留：表格【单元格文字内容】（仅丢弃边框 | + -）
#   丢弃：表格分隔行（仅由 | + - : 空格组成的行）
#   丢弃：代码块(```...```)、图片(![]())、HTML 标签、行内代码(`)
#   转换：### → ## (L3 标题)、链接 [文字](url) → 文字、1) → 1.、A./a./i. → -
#   插入：不同 L3 之间插入 ---
#
# 设计原则：保留 100% 文字语义；只去除格式符号，不丢弃文字内容

param(
    [Parameter(Mandatory=$true)] [string]$InputFile,
    [Parameter(Mandatory=$true)] [string]$OutputFile,
    [Parameter(Mandatory=$true)] [int]$StartLine,
    [Parameter(Mandatory=$true)] [int]$EndLine,
    [Parameter(Mandatory=$true)] [string]$L2Title
)

# ---------- 判定：是否为表格分隔行（仅含 | + - : 空格） ----------
function IsTableSeparator {
    param([string]$Line)
    $trim = $Line.Trim()
    if ($trim.Length -eq 0) { return $false }
    return ($trim -match '^[\|\+\-: ]+$')
}

# ---------- 判定：是否为表格内容行（以 | 开头） ----------
function IsTableRow {
    param([string]$Line)
    return ($Line -match '^\s*\|')
}

# ---------- 表格行提取：拆分单元格，保留文字，过滤空单元格 ----------
function ExtractTableCells {
    param([string]$Line)
    # 去掉首尾 |
    $body = $Line.Trim()
    $body = $body -replace '^\|', ''
    $body = $body -replace '\|$', ''
    # 按 | 拆分
    $cells = $body -split '\|'
    # 清洗每个单元格
    $clean = @()
    foreach ($c in $cells) {
        $t = $c.Trim()
        # 单元格内：去除图片/链接/HTML/行内代码
        $t = $t -replace '!\[[^\]]*\]\([^\)]*\)', ''
        $t = $t -replace '\[([^\]]+)\]\([^\)]+\)', '$1'
        $t = $t -replace '<[^>]+>', ''
        $t = $t -replace '`([^`]+)`', '$1'
        $t = $t -replace ' {2,}', ' '
        $t = $t.Trim()
        if ($t.Length -gt 0) { $clean += $t }
    }
    if ($clean.Count -eq 0) { return $null }
    # 2列：first: second 风格（适合字段定义型表格）
    if ($clean.Count -eq 2) { return ('- ' + $clean[0] + '：' + $clean[1]) }
    # 多列：用 ` | ` 连接（保留对齐感）
    return ('- ' + ($clean -join ' | '))
}

# ---------- 普通行处理：返回 $null 表示丢弃 ----------
function ProcessLine {
    param([string]$Line)
    $text = $Line

    # 1. 图片整行
    if ($text -match '^\s*!\[[^\]]*\]\([^\)]*\)\s*$') { return $null }

    # 2. 引用 > 去掉前缀
    $text = $text -replace '^\s*>\s?', ''

    # 3. 行内：图片 → 删除
    $text = $text -replace '!\[[^\]]*\]\([^\)]*\)', ''

    # 4. 行内：链接 → 文字
    $text = $text -replace '\[([^\]]+)\]\([^\)]+\)', '$1'

    # 5. 行内：HTML 标签 → 删除
    $text = $text -replace '<[^>]+>', ''

    # 6. 行内：行内代码 → 内容
    $text = $text -replace '`([^`]+)`', '$1'

    # 7. 列表标记标准化（保留前导空格以维持嵌套层级）
    $text = $text -replace '^(\s*)(\d+)\) ',  '$1$2. '
    $text = $text -replace '^(\s*)\* ',        '$1- '
    $text = $text -replace '^(\s*)([A-Z])\. ', '$1- '
    $text = $text -replace '^(\s*)([a-z])\. ', '$1  - '
    $text = $text -replace '^(\s*)([ivx]+)\. ','$1    - '

    # 8. 制表符 → 2空格（维持视觉缩进，不剥离前导空格）
    $text = $text -replace "`t", '  '

    # 9. 行内多余空格合并（保护行首缩进）
    $text = $text -replace '(?<=\S) {2,}', ' '

    # 10. 标题层级转换：### → ##，#### 及以上 → ###；原 # 和 ## 标题（即 L1/L2）整行丢弃（已由 header 补位）
    if     ($text -match '^### ')   { $text = $text -replace '^### ',   '## ' }
    elseif ($text -match '^#### ')  { $text = $text -replace '^#### ',  '### ' }
    elseif ($text -match '^##### ') { $text = $text -replace '^##### ', '### ' }
    elseif ($text -match '^###### '){ $text = $text -replace '^###### ','### ' }
    elseif ($text -match '^## ')    { return $null }
    elseif ($text -match '^# ')     { return $null }

    return $text.TrimEnd()
}

# ---------- 主流程 ----------
$allLines = Get-Content -Encoding UTF8 $InputFile
$rawLines = $allLines[($StartLine-1)..($EndLine-1)]

$processed = @()
$inCodeBlock = $false

foreach ($raw in $rawLines) {
    # 代码块：跳过 ``` 之间所有内容（含围栏行）
    if ($raw -match '^\s*```') {
        $inCodeBlock = -not $inCodeBlock
        continue
    }
    if ($inCodeBlock) { continue }

    # 表格分隔行（含 RST 风格 +----+ 和 MD 风格 |----|）→ 丢弃
    if (IsTableSeparator $raw) { continue }

    # 表格内容行 → 提取单元格文字
    if (IsTableRow $raw) {
        $cellLine = ExtractTableCells $raw
        if ($null -ne $cellLine) { $processed += $cellLine }
        continue
    }

    # 普通行
    $line = ProcessLine $raw
    if ($null -eq $line) { continue }
    $processed += $line
}

# 合并并清理空行
$content = $processed -join "`n"
$content = $content -replace "`n{3,}", "`n`n"
$content = $content.Trim()

# 在 ## L3 标题前插入 --- 分隔符（首个除外）
$finalLines = @()
$firstHeading = $true
foreach ($l in ($content -split "`n")) {
    if ($l -match '^## ') {
        if ($firstHeading) {
            $firstHeading = $false
        } else {
            $finalLines += ''
            $finalLines += '---'
            $finalLines += ''
        }
    }
    $finalLines += $l
}
$content = ($finalLines -join "`n").Trim()

# 添加 L2 文档头
$header = "# $L2Title`n`n"
$final = $header + $content + "`n"

# 写入
$final | Set-Content -Encoding UTF8 -NoNewline $OutputFile

# 校验摘要
$resultLines = $final -split "`n"
$cnt = @{
    Lines      = $resultLines.Count
    L2Heading  = ($resultLines | Where-Object { $_ -match '^# ' }).Count
    L3Heading  = ($resultLines | Where-Object { $_ -match '^## ' }).Count
    L4Heading  = ($resultLines | Where-Object { $_ -match '^### ' }).Count
    Separators = ($resultLines | Where-Object { $_ -eq '---' }).Count
    PipeLines  = ($resultLines | Where-Object { $_ -match '\|' }).Count
    HtmlLines  = ($resultLines | Where-Object { $_ -match '<[^>]+>' }).Count
}
Write-Host "Done: $OutputFile"
Write-Host ("  Lines:{0}  L2:{1}  L3(##):{2}  L4(###):{3}  Sep:{4}  Pipes:{5}  Html:{6}" -f `
    $cnt.Lines, $cnt.L2Heading, $cnt.L3Heading, $cnt.L4Heading, $cnt.Separators, $cnt.PipeLines, $cnt.HtmlLines)
