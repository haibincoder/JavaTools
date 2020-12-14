package com.bincoder.force.ditect;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 力引导算法，用于echarts关系图
 */
public class Main {
	private static List<Node> sNodeArrayList = new ArrayList<>();
	private static List<Link> sLinkArrayList;

	public static void main(String[] args) {
		getData();
//		getRandomData();
		//随机生成坐标. Generate coordinates randomly.
		double initialX, initialY, initialSize = 40.0;
		for (Node node : sNodeArrayList) {
			initialX = 0 + CollisionGenerator.CANVAS_WIDTH * .5;
			initialY = 0 + CollisionGenerator.CANVAS_HEIGHT * .5;
			node.setXPosition(initialX + initialSize * (Math.random() - .5));
			node.setYPosition(initialY + initialSize * (Math.random() - .5));
		}

		CollisionGenerator collisionGenerator = new CollisionGenerator(sNodeArrayList, sLinkArrayList);
		//迭代200次. Iterate 200 times.
		for (int i = 0; i < 200; i++) {
			collisionGenerator.collide();
		}
		ResultEntity resultEntity = new ResultEntity();
		resultEntity.nodes = collisionGenerator.getNodeList();
		resultEntity.links = sLinkArrayList;
		String res = new Gson().toJson(resultEntity);
		System.out.println(res);
	}

	static class ResultEntity {
		List<Node> nodes;
		List<Link> links;
	}

	/**
	 * Manually constructed data. For reference only.
	 */
	private static void getData() {
		sLinkArrayList = Utils.getEdges();
		Set<Node> nodeSet = new HashSet<>();
		for(Link link : sLinkArrayList){
			nodeSet.add(new Node(link.getSource()));
			nodeSet.add(new Node(link.getTarget()));
		}
		sNodeArrayList.addAll(nodeSet);
	}

	/**
	 * 随机构造一些node和的edge。For reference only.
	 */
	private static void getRandomData() {
		sLinkArrayList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Node node = new Node(i);
			sNodeArrayList.add(node);
		}
		for (int i = 0; i < 20; i++) {
			int edgeCount = (int) (Math.random() * 8);
			System.out.println(edgeCount);
			for (int j = 0; j < edgeCount; j++) {
				int targetId = (int) (Math.random() * 20);
				Link link = new Link(i, targetId);
				sLinkArrayList.add(link);
			}
		}
	}
}
