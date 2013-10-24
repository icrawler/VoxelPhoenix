package com.phoenix.voxel;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleScaleDomain;

public class WorldUtils {
	private long seed;
	public ModuleAutoCorrect heightMap;
	
	public WorldUtils(long seed) {
		this.seed = seed;
		heightMap = new ModuleAutoCorrect();
	}
	
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
