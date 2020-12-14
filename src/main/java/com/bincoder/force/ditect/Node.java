package com.bincoder.force.ditect;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Node {
	private double x;
	private double y;
	/**
	 * 节点Id
	 */
	private String id;
	/**
	 * 节点展示名
	 */
	private String name;
	/**
     *  节点类型
	 */
	private String category;

	public Node(int id) {
		this.id = id + "";
	}
	public Node(String id){
		this.id = id;
	}

	public void setXPosition(double x) {
		this.x = Utils.getInt(x);
	}

	public void setYPosition(double y) {
		this.y = Utils.getInt(y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getKey() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o){
		// 自反性
		if (this == o) return true;
		// 任何对象不等于null，比较是否为同一类型
		if (!(o instanceof Node)) return false;
		// 强制类型转换
		Node node = (Node) o;
		// 比较属性值
		return StringUtils.equals(getKey(), node.getKey());
	}

	@Override
	public int hashCode(){
		return Objects.hash(getKey(), getName());
	}
}
