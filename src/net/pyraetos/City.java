package net.pyraetos;

import java.util.Random;

import net.pyraetos.util.Sys;

public class City{

	private Random rand;
	private byte[][] cityMap;
	
	public static final byte NOTHING = 0;
	public static final byte ROAD = 1;
	public static final byte HOUSE = 2;
	
	public City() {
		this.rand = new Random();
		cityMap = new byte[256][];
		int dir = rand.nextInt(4);
		genCity(128, 128, dir, 0);
	}
	
	private void genCity(int x, int y, int dir, int deep) {
		int[] xy = {x,y};
		int curItr = 0;
		while(true) {
			set(xy[0], xy[1], ROAD);
			if(interChance(deep)) {
				int newDir = intersection(dir);
				int[] newXY = xform(xy, newDir);
				genCity(newXY[0], newXY[1], newDir, deep+1);
			}
			if(endChance(deep)) {
				return;
			}
			curItr++;
			xform(xy, dir);
		}
	}
	
	private void set(int x, int y, byte value) {
		Sys.debug(x);
		if(cityMap[x] == null) cityMap[x] = new byte[256];
		cityMap[x][y] = value;
	}
	
	public int valueAt(int x, int y) {
		if(cityMap[x] == null) return NOTHING;
		return cityMap[x][y];
	}
	
	private Boolean interChance(int deep) {
		return Sys.chance(0.05 + deep * 0.1);
	}
	
	private Boolean endChance(int deep) {
		return Sys.chance(0.05 + deep * 0.1);
	}
	
	private int[] xform(int[] xy, int dir) {
		switch(dir) {
		case(Sys.NORTH):{
			xy[1] += 1;
			break;
		}
		case(Sys.SOUTH):{
			xy[1] -= 1;
			break;
		}
		case(Sys.EAST):{
			xy[0] += 1;
			break;
		}
		case(Sys.WEST):{
			xy[0] -= 1;
			break;
		}
		}
		return xy;
	}
	
	private int intersection(int dir) {
		if(dir == Sys.NORTH || dir == Sys.SOUTH) {
			return Sys.chance(0.5) ? Sys.WEST : Sys.EAST;
		}
		if(dir == Sys.EAST || dir == Sys.WEST) {
			return Sys.chance(0.5) ? Sys.NORTH : Sys.SOUTH;
		}
		return dir;
	}
	
}
