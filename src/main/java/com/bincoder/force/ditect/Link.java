package com.bincoder.force.ditect;

public class Link {
	private String source;
	private String target;

	public Link(int source, int target) {
		this.source = source + "";
		this.target = target + "";
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
