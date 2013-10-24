package com.phoenix.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.lights.Lights;

public class VoxelWorld {
	public int ViewDistance;
	public Chunk[][] activeChunks;
	public int xPos;
	public int yPos;
	public final long worldSeed;
	public WorldUtils worldGen;
	public PerspectiveCamera mainCamera;
	public VoxelCamInputController camController;

	public VoxelWorld(int viewDistance, long worldSeed) {
		this.ViewDistance = viewDistance;
		activeChunks = new Chunk[2*viewDistance+1][2*viewDistance+1];
		xPos = 0;
		yPos = 0;
		
		mainCamera = new PerspectiveCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mainCamera.position.set(500, 500, 500);
		mainCamera.near = 16f;
		mainCamera.far = 4000f;
		mainCamera.lookAt(0, 0, 0);
		mainCamera.update();
		
		camController = new VoxelCamInputController(mainCamera);
		camController.translateUnits = 128f;
		
		for (int z=0; z < 2*viewDistance+1; z++)
			for (int x=0; x < 2*viewDistance+1; x++)
				activeChunks[z][x] = new Chunk(worldSeed, z-viewDistance, x-viewDistance);
		this.worldSeed = worldSeed;
		worldGen = new WorldUtils(worldSeed);
		worldGen.GenerateHeightMap(2);
	}
	
	public void dispose() {
		for (int z=0; z < 2*ViewDistance+1; z++) {
			for (int x=0; x < 2*ViewDistance+1; x++) {
				activeChunks[z][x].dispose();
			}
		}
	}
	
	public void rebuildChunks() {
		for (int z=0; z < 2*ViewDistance+1; z++) {
			for (int x=0; x < 2*ViewDistance+1; x++) {
				System.out.println("world: x: " + (x-ViewDistance-xPos) + ", z: " + (z-ViewDistance-yPos));
				activeChunks[z][x].xPos = x-ViewDistance-xPos;
				activeChunks[z][x].yPos = z-ViewDistance-yPos;
				//activeChunks[z][x].GenerateFlatTerrain(1);
				worldGen.GenerateHeightMapTerrain(activeChunks[z][x].blocks, 
												  16, 
												  4, 
												  2,  
												  x, 
												  z);
				activeChunks[z][x].CullBlockFaces();
				activeChunks[z][x].CreateFaces();
				activeChunks[z][x].MergeFaces();
				activeChunks[z][x].CreateMesh();
			}
		}
	}
	
	public void printChunks() {
		for (int z=0; z < 2*ViewDistance+1; z++) {
			for (int x=0; x < 2*ViewDistance+1; x++) {
				System.out.println("Current Chunk. x: " + x + ", y:" + z);
				//activeChunks[z][x].GenerateChunkAltitudes();
			}
		}
	}
	
	public void renderChunks(ModelBatch batch, Lights lights) {
		for (int z=0; z < 2*ViewDistance+1; z++) {
			for (int x=0; x < 2*ViewDistance+1; x++) {
				batch.render(activeChunks[z][x].chunkInstance, lights);
			}
		}
	}
}
