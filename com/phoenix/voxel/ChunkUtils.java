package com.phoenix.voxel;

public class ChunkUtils {

	public static BlockFace[][][][] OptimizeChunkMesh(BlockFace[][][][] chunkFaces) {
		BlockFace[][][][] result = new BlockFace[128][16][16][6];
		
		// sweep y
		for (int y=0; y < 128; y++) {
			for (int z=0; z < 16; z++) {
				for (int i=0; i < 2; i++) {
					int prevLength = 0;
					int prevType = 0;
	 				for (int x=0; x < 16; x++) {
	 					int currentLength = 0;
	 					int currentType = 0;
	 					
	 					if (chunkFaces[y][z][x][2+i] != null) {
	 						currentLength = chunkFaces[y][z][x][2+i].w;
	 						currentType = chunkFaces[y][z][x][2+i].sideType;
	 					}
	 					
	 					if (x == 15) {
	 						if (currentLength != 0 && prevLength == 0)
	 							result[y][z][15][2+i] = new BlockFace(15, y, z, currentLength, 1, 2+i, currentType);
	 						else if (prevLength != 0)
	 							result[y][z][x-prevLength][2+i] = new BlockFace(x-prevLength, y, z, prevLength+1, 1, 2+i, prevType);
	 						prevLength = 0;
	 						prevType = 0;
	 						break;
	 					}
	 					
	 					if (prevLength == 0) {
	 						prevLength = currentLength;
	 						prevType = currentType;
	 					}
	 					else {
	 						if (currentLength == 0 || currentType != prevType) {
	 							result[y][z][x-prevLength][2+i] = new BlockFace(x-prevLength, y, z, prevLength, 1, 2+i, prevType);
	 							prevLength = currentLength;
	 							prevType = currentType;
	 						}
	 						else {
	 							prevLength++;
	 						}
	 					}
					}
				}
			}
		}
		
		// sweep z
		for (int z=0; z < 16; z++) {
			for (int y=0; y < 128; y++) {
				for (int i=0; i < 2; i++) {
					int prevLength = 0;
					int prevType = 0;
	 				for (int x=0; x < 16; x++) {
	 					int currentLength = 0;
	 					int currentType = 0;
	 					
	 					if (chunkFaces[y][z][x][i] != null) {
	 						currentLength = chunkFaces[y][z][x][i].w;
	 						currentType = chunkFaces[y][z][x][i].sideType;
	 					}
	 					
	 					if (x == 15) {
	 						if (currentLength != 0 && prevLength == 0)
	 							result[y][z][15][i] = new BlockFace(15, y, z, currentLength, 1, i, currentType);
	 						else if (prevLength != 0)
	 							result[y][z][x-prevLength][i] = new BlockFace(x-prevLength, y, z, prevLength+1, 1, i, prevType);
	 						break;
	 					}
	 					
	 					if (prevLength == 0) {
	 						prevLength = currentLength;
	 						prevType = currentType;
	 					}
	 					else {
	 						if (currentLength == 0 || currentType != prevType) {
	 							result[y][z][x-prevLength][i] = new BlockFace(x-prevLength, y, z, prevLength, 1, i, prevType);
	 							prevLength = currentLength;
	 							prevType = currentType;
	 						}
	 						else {
	 							prevLength++;
	 						}
	 					}
					}
				}
			}
		}
		
		// sweep z
		for (int y=0; y < 128; y++) {
			for (int x=0; x < 16; x++) {
				for (int i=0; i < 2; i++) {
					int prevLength = 0;
					int prevType = 0;
	 				for (int z=0; z < 16; z++) {
	 					int currentLength = 0;
	 					int currentType = 0;
	 					
	 					if (chunkFaces[y][z][x][4+i] != null) {
	 						currentLength = chunkFaces[y][z][x][4+i].w;
	 						currentType = chunkFaces[y][z][x][4+i].sideType;
	 					}
	 					
	 					if (z == 15) {
	 						if (currentLength != 0 && prevLength == 0)
	 							result[y][15][x][4+i] = new BlockFace(x, y, 15, currentLength, 1, 4+i, currentType);
	 						else if (prevLength != 0)
	 							result[y][z-prevLength][x][4+i] = new BlockFace(x, y, z-prevLength, prevLength+1, 1, 4+i, prevType);
	 						break;
	 					}
	 					
	 					if (prevLength == 0) {
	 						prevLength = currentLength;
	 						prevType = currentType;
	 					}
	 					else {
	 						if (currentLength == 0 || currentType != prevType) {
	 							result[y][z-prevLength][x][4+i] = new BlockFace(x, y, z-prevLength, prevLength, 1, 4+i, prevType);
	 							prevLength = currentLength;
	 							prevType = currentType;
	 						}
	 						else {
	 							prevLength++;
	 						}
	 					}
					}
				}
			}
		}
		
		System.out.println("well that worked");
		
		return result;
	}
	
	public static void countFaces(BlockFace[][][][] faces, int orientation) {
		int count = 0;
		for (int y=0; y < 128; y++) {
			for (int z=0; z < 16; z++) {
				for (int x=0; x < 16; x++) {
					if (faces[y][z][x][orientation] != null) {
						count++;
					}
				}
			}
		}
		System.out.println("Number of faces were " + count);
	}

}
