package cn.seehoo.spg.bizcom.gd.vo;

import lombok.Data;

@Data
public class GdMapCheckVO {
	//省
	private String province;
	//市
	private String city;
	//区
	private String district;
	//匹配级别
	private String level;
}