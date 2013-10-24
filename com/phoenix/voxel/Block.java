package com.phoenix.voxel;

public class Block {
	public boolean active;
	private int blockType;
	public int lightLevel;
	public BlockSide[] sides;
	
	public Block(int blockType) {
		this.blockType = blockType;
		lightLevel = 15;
		sides = new BlockSide[6];
		active = true;
		for (int i=0; i < 6; i++)
			sides[i] = new BlockSide(15, i);
	}
	
	public int getBlockType() {
		return blockType;
	}
}
