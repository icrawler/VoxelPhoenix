package com.phoenix.voxel;

public class BlockSide {
	public int lightLevel;
	
	/**
	 * 0 - +z
	 * 1 - -z
	 * 2 - +y
	 * 3 - -y
	 * 4 - +x
	 * 5 - -x
	 */
	public int index;
	
	public boolean isActive;

	public BlockSide(int lightLevel, int index) {
		this.lightLevel = lightLevel;
		this.index = index;
		isActive = true;
	}

}
