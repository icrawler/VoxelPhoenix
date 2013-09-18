package com.phoenix.voxel;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class Minimap {
	public int[][] blockTypes;
	private Pixmap map;

	public Minimap(VoxelWorld world) {
		blockTypes = new int[world.ViewDistance*2+1][world.ViewDistance*2+1];
		map = new Pixmap(128, 128, Format.RGBA8888);
		for (int y=0; y < blockTypes.length; y++) {
			for (int x=0; x < blockTypes.length; x++) {
				blockTypes[y][x];
			}
		}
	}

}
