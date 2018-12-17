package net.pyraetos;

import java.util.Random;

import net.pyraetos.util.Sys;

public class City{

	private Random rand;
	private byte[][] cityMap;
	private int size;
	
	public static final byte NOTHING = 0;
	public static final byte ROAD = 1;
	public static final byte HOUSE = 2;
	public static final byte SPECIAL = 64;
	
	public static final int SMALL = 5;
	public static final int MEDIUM = 10;
	public static final int LARGE = 20;
	
	public static final long SEED = Sys.randomSeed();
	
	public City(int size) {
		this.size = size;
		this.rand = new Random(SEED);
		cityMap = new byte[256][];
		int dir = rand.nextInt(4);
		genCity(128, 128, dir, 0);
	}
	
	private void genCity(int x, int y, int dir, int deep) {
		int[] xy = {x,y};
		int roadLen = 0;
		int sinceLastInter = 0;
		while(true) {
			if(!suitable(xy[0], xy[1], dir)) {
				return;
			}
			set(xy[0], xy[1], ROAD);
			if(interChance(deep, sinceLastInter)) {
				int newDir = intersection(dir);
				int[] newXY = xform(xy.clone(), newDir);
				genCity(newXY[0], newXY[1], newDir, deep+1);
				sinceLastInter = 0;
			}
			if(endChance(deep, roadLen)) {
				return;
			}
			if(houseChance(roadLen)) {
				tryEmplaceHouse(xy[0], xy[1], dir);
			}
			roadLen++;
			sinceLastInter++;
			xform(xy, dir);
		}
	}
	
	private boolean suitable(int x, int y, int dir) {
		if(x < 3 || x > 252 || y < 3 || y > 252) return false;
		int x1, x2;
		int y1, y2;
		int xmid = x, xmid1 = x;
		int ymid = y, ymid1 = y;
		switch(dir) {
		case(Sys.NORTH):{
			x1 = x-1;
			x2 = x+1;
			y1 = y2 = y+1;
			ymid++;
			ymid1 += 2;
			break;
		}
		case(Sys.SOUTH):{
			x1 = x-1;
			x2 = x+1;
			y1 = y2 = y-1;
			ymid--;
			ymid1 -= 2;
			break;
		}
		case(Sys.EAST):{
			y1 = y-1;
			y2 = y+1;
			x1 = x2 = x+1;
			xmid++;
			xmid1 += 2;
			break;
		}
		case(Sys.WEST):{
			y1 = y-1;
			y2 = y+1;
			x1 = x2 = x-1;
			xmid--;
			xmid1 -= 2;
			break;
		}
		default: return true;
		}
		if(valueAt(xmid, ymid) != NOTHING) {
			return valueAt(xmid1, ymid1) == NOTHING;
		}
		return valueAt(x1, y1) == NOTHING && valueAt(x2, y2) == NOTHING;
	}
	
	private void tryEmplaceHouse(int x, int y, int dir) {
		int x1 = x, x2 = x;
		int y1 = y, y2 = y;
		switch(dir) {
		case(Sys.NORTH):
		case(Sys.SOUTH):{
			x1 = x-1;
			x2 = x+1;
			break;
		}
		case(Sys.EAST):
		case(Sys.WEST):{
			y1 = y-1;
			y2 = y+1;
			break;
		}
		}
		if(x1 < 0 || x1 > 255 || y1 < 0 || y1 > 255) return;
		if(x2 < 0 || x2 > 255 || y2 < 0 || y2 > 255) return;
		if(Sys.chance(0.5, rand)) {
			if(Sys.chance(0.5, rand)) {
				if(valueAt(x2, y2) == NOTHING) set(x2, y2, HOUSE);
			}
			if(valueAt(x1, y1) == NOTHING) set(x1, y1, HOUSE);
		}else if(valueAt(x2, y2) == NOTHING) set(x2, y2, HOUSE);
	}
	
	private void set(int x, int y, byte value) {
		if(cityMap[x] == null) cityMap[x] = new byte[256];
		cityMap[x][y] = value;
	}
	
	public int valueAt(int x, int y) {
		if(cityMap[x] == null) return NOTHING;
		return cityMap[x][y];
	}
	
	private boolean interChance(int deep, int sinceLastInter) {
		if(sinceLastInter < 4) return false;
		return Sys.chance(0.1 + ((double)deep) * 0.01, rand);
	}
	
	private boolean endChance(int deep, int roadLen) {
		return Sys.chance(Math.pow(2d - 0.02*((double)roadLen), ((double)deep)-size), rand);
	}
	
	private boolean houseChance(int roadLen) {
		return Sys.chance(0.1 + 0.1 * Math.sin(4d * roadLen), rand);
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
			return Sys.chance(0.5, rand) ? Sys.WEST : Sys.EAST;
		}
		if(dir == Sys.EAST || dir == Sys.WEST) {
			return Sys.chance(0.5, rand) ? Sys.NORTH : Sys.SOUTH;
		}
		return dir;
	}
	
}
