package com.phoenix.voxel;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;

/**
 * WorldUtils Class
 * 
 * @author Phoenix Enero
 */
public class WorldUtils {
	private long seed;
	public ModuleAutoCorrect heightMap;
	
	/**
	 * Creates new WorldUtils class with a specific seed for the world generators
	 * 
	 * @param seed the seed for the world generators
	 */
	public WorldUtils(long seed) {
		this.seed = seed;
		heightMap = new ModuleAutoCorrect();
	}
	
	/**
	 * Generates heightmap
	 * 
	 * @param octaves Number of octaves
	 */
	public void GenerateHeightMap(int octaves) {
		ModuleFractal fracNoise = new ModuleFractal();
		fracNoise.setSeed(seed);
		fracNoise.setAllSourceBasisTypes(BasisType.SIMPLEX);
		fracNoise.setType(FractalType.MULTI);
		fracNoise.setNumOctaves(octaves);
		
		heightMap.setSource(fracNoise);
		heightMap.setRange(0, 1);
		heightMap.calculate();
	}
	
	/**
	 * Generates terrain based on the heightmap
	 * 
	 * @param blocks a 3D block array
	 * @param groundLevel the ground level of the map
	 * @param maxHeight the maximum height of the map
	 * @param scale how large are the features
	 * @param ox x offset
	 * @param oz z offset
	 */
	public void GenerateHeightMapTerrain(Block[][][] blocks, 
										 int groundLevel,
										 int maxHeight,
										 int scale, 
										 int ox, 
										 int oz) {
		for (int z=0; z < 16; z++) {
			for (int x=0; x < 16; x++) {
				double height = heightMap.get((x+ox*16d)/(16d*scale), 
											  (z+oz*16d)/(16d*scale));
				for (int y=0; y < 128; y++) {
					if (y < Math.floor(height*maxHeight + groundLevel)) {
						blocks[y][z][x] = new Block(0);
					}
					else if (y == Math.floor(height*maxHeight + groundLevel)) {
						blocks[y][z][x] = new Block(BlockAttributes.Types.Grass);
					}
				}
			}
		}
	}
}
