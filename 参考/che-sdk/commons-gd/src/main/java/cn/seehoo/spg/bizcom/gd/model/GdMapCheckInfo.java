package cn.seehoo.spg.bizcom.gd.model;

import lombok.Data;

@Data
public class GdMapCheckInfo {
	//省
	private String province;
	//市
	private String city;
	//区
	private String district;
	//匹配级别
	private String level;
}