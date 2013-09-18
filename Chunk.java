package com.phoenix.voxel;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Chunk {
	public Block[][][] blocks;
	public int blockSize;
	public int xPos;
	public int yPos;
	public long chunkSeed;
	private ModelBuilder modelBuilder;
	private Model chunkModel;
	//private BlockCreator blockCreator;
	public ModelInstance chunkInstance;
	public BlockFace[][][][] blockFaces;
	
	public Chunk(long worldSeed, int x, int y) {
		blockSize = 16;
		blocks = new Block[128][16][16];
		this.xPos = x;
		this.yPos = y;
		chunkSeed = worldSeed;
		blockFaces = new BlockFace[128][16][16][6]; // y, z, x, numsides
		modelBuilder = new ModelBuilder();
	}
	
	public void dispose() {
		chunkInstance.model.dispose();
	}
	
	public void GenerateFlatTerrain(int height) {
		for (int y=0; y < 128; y++)
			for (int z=0; z < 16; z++)
				for (int x=0; x < 16; x++)
					if (y < height)
						blocks[y][z][x] = new Block(0);
	}
	
	public void GenerateChunkAltitudes() {
		for (int z=0; z < 16; z++) {
			System.out.print("|");
			for (int x=0; x < 16; x++) {
				int alt = 0;
				for (int y=0; y < 128; y++) {
					if (blocks[y][z][x] != null)
						if (y+1 > alt)
							alt = y+1;
				}
				System.out.print(alt + "|");
			}
			System.out.print("\n");
		}
	}
	
	public void CullBlockFaces() {
		for (int y=0; y < 128; y++) {
			for (int z=0; z < 16; z++) {
				for (int x=0; x < 16; x++) {
					if (blocks[y][z][x] != null) {
						// cull faces aligned at the x direction
						if (x > 0 && x < 15) {
							if (blocks[y][z][x-1] != null)
								blocks[y][z][x-1].sides[4].isActive = false;
							else
								blocks[y][z][x].sides[5].isActive = true;
							if (blocks[y][z][x+1] != null)
								blocks[y][z][x+1].sides[5].isActive = false;
							else
								blocks[y][z][x].sides[4].isActive = true;
						}
						else if (x == 0) {
							if (blocks[y][z][1] != null)
								blocks[y][z][1].sides[5].isActive = false;
							else
								blocks[y][z][0].sides[4].isActive = true;
						}
						else if (x == 15) {
							if (blocks[y][z][14] != null)
								blocks[y][z][14].sides[4].isActive = false;
							else
								blocks[y][z][15].sides[5].isActive = true;
						}
						
						// cull faces aligned at the z direction
						if (z > 0 && z < 15) {
							if (blocks[y][z-1][x] != null)
								blocks[y][z-1][x].sides[0].isActive = false;
							else
								blocks[y][z][x].sides[1].isActive = true;
							if (blocks[y][z+1][x] != null)
								blocks[y][z+1][x].sides[1].isActive = false;
							else
								blocks[y][z][x].sides[0].isActive = true;
						}
						else if (z == 0) {
							if (blocks[y][1][x] != null)
								blocks[y][1][x].sides[1].isActive = false;
							else
								blocks[y][0][x].sides[0].isActive = true;
						}
						else if (z == 15) {
							if (blocks[y][14][x] != null)
								blocks[y][14][x].sides[0].isActive = false;
							else
								blocks[y][15][x].sides[1].isActive = true;
						}
						
						// cull faces aligned at the y direction
						if (y > 0 && y < 127) {
							if (blocks[y-1][z][x] != null)
								blocks[y-1][z][x].sides[2].isActive = false;
							else
								blocks[y][z][x].sides[3].isActive = true;
							if (blocks[y+1][z][x] != null)
								blocks[y+1][z][x].sides[3].isActive = false;
							else
								blocks[y][z][x].sides[2].isActive = true;
						}
						else if (y == 0) {
							if (blocks[1][z][x] != null)
								blocks[1][z][x].sides[3].isActive = false;
							else
								blocks[0][z][x].sides[2].isActive = true;
						}
						else if (y == 127) {
							if (blocks[126][z][x] != null)
								blocks[126][z][x].sides[2].isActive = false;
							else
								blocks[127][z][x].sides[3].isActive = true;
						}
					}
				}
			}
		}
	}
	
	public void CreateFaces() {
		for (int y=0; y < 128; y++) {
			for (int z=0; z < 16; z++) {
				for (int x=0; x < 16; x++) {
					if (blocks[y][z][x] != null) {
						if (blocks[y][z][x].sides[0].isActive)
							blockFaces[y][z][x][0] = new BlockFace(x, y, z, 1, 1, 0, blocks[y][z][x].getBlockType());
						if (blocks[y][z][x].sides[1].isActive)
							blockFaces[y][z][x][1] = new BlockFace(x, y, z, 1, 1, 1, blocks[y][z][x].getBlockType());
						if (blocks[y][z][x].sides[2].isActive)
							blockFaces[y][z][x][2] = new BlockFace(x, y, z, 1, 1, 2, blocks[y][z][x].getBlockType());
						if (blocks[y][z][x].sides[3].isActive)
							blockFaces[y][z][x][3] = new BlockFace(x, y, z, 1, 1, 3, blocks[y][z][x].getBlockType());
						if (blocks[y][z][x].sides[4].isActive)
							blockFaces[y][z][x][4] = new BlockFace(x, y, z, 1, 1, 4, blocks[y][z][x].getBlockType());
						if (blocks[y][z][x].sides[5].isActive)
							blockFaces[y][z][x][5] = new BlockFace(x, y, z, 1, 1, 5, blocks[y][z][x].getBlockType());
					}
				}
			}
		}
	}
	
	public void MergeFaces() {
		BlockFace[][][][] resultFaces = blockFaces;
		
		resultFaces = ChunkUtils.OptimizeChunkMesh(blockFaces);
		
		blockFaces = resultFaces;
	}
	
	public void CreateMesh() {
		if (chunkModel != null)
			chunkModel.dispose();
		
		modelBuilder.begin();
		
		for (int y=0; y < 128; y++) {
			for (int z=0; z < 16; z++) {
				for (int x=0; x < 16; x++) {
					if (blocks[y][z][x] != null) {
						//blockCreator.createBlock(modelBuilder, blocks[y][z][x], blockSize, x, y, z);
						for (int i=0; i < 6; i++) {
							if (blockFaces[y][z][x][i] != null)
								blockFaces[y][z][x][i].createFace(modelBuilder, 16);
						}
					}
				}
			}
		}
		
		chunkModel = modelBuilder.end();
		
		chunkInstance = new ModelInstance(chunkModel, xPos*blockSize*16, 0, yPos*blockSize*16);
	}
}
