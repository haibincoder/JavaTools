package com.bincoder.force.ditect;

import com.bincoder.force.ditect.data.DataSet2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class Utils {
	// 四舍五入把double转化int整型，0.5进一，小于0.5不进一
	static int getInt(double number) {
		BigDecimal bd = new BigDecimal(number).setScale(0, BigDecimal.ROUND_HALF_UP);
		return Integer.parseInt(bd.toString());
	}

	static List<Link> getEdges() {
		String dataSet = getDataSet();
		if (dataSet == null) {
			return null;
		}
		String notRaw[] = dataSet.split(";");
		List<Link> linkList = new ArrayList<>();
		for (String str : notRaw) {
			String[] pair = str.split(" ");
			linkList.add(new Link(Integer.parseInt(pair[0]), Integer.parseInt(pair[1])));
		}
		return linkList;
	}

	private static String getDataSet() {
		return DataSet2.dataSet;
	}
}