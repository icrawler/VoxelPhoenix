package com.phoenix.voxel;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class BlockCreator {
	
	/*public Model createBox(float s, 
			boolean front, 
			boolean back, 
			boolean left, 
			boolean right, 
			boolean top, 
			boolean bottom) {
		Material currentMaterial = new Material(ColorAttribute.createDiffuse(new Color(0.0f, 1.0f, 0.0f, 1.0f)));
		Model boxModel;  = modelBuilder.createBox(s, s, s, GL10.GL_TRIANGLES,
              new Material(ColorAttribute.createDiffuse(new Color(0.0f, 0.7f, 0.0f, 1.0f))),
              Usage.Position | Usage.Normal);
		
		Vector3 corner1 = new Vector3(0, 0, s);
		Vector3 corner2 = new Vector3(s, 0, s);
		Vector3 corner3 = new Vector3(s, s, s);
		Vector3 corner4 = new Vector3(0, s, s);
		Vector3 corner5 = new Vector3(0, 0, 0);
		Vector3 corner6 = new Vector3(s, 0, 0);
		Vector3 corner7 = new Vector3(s, s, 0);
		Vector3 corner8 = new Vector3(0, s, 0);
		
		modelBuilder.begin();
		
		// check sides
		if (front) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
						.rect(corner1, corner2, corner3, corner4, new Vector3(0, 0, 1));
		}
		if (back) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner8, corner7, corner6, corner5, new Vector3(0, 0, -1));
		}
		if (top) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner4, corner3, corner7, corner8, new Vector3(0, 1, 0));
		}
		if (bottom) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner2, corner1, corner5, corner6, new Vector3(0, -1, 0));
		}
		if (right) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner3, corner2, corner6, corner7, new Vector3(1, 0, 0));
		}
		if (left) {
			modelBuilder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner5, corner1, corner4, corner8, new Vector3(-1, 0, 0));
		}
		
		boxModel = modelBuilder.end();

		return boxModel;
	}*/

	public void createBlock(ModelBuilder builder, Block block, int s, int x, int y, int z) {
		Material currentMaterial = new Material(ColorAttribute.createDiffuse(BlockAttributes.Colors[block.getBlockType()]));
		int ox = x*s;
		int oy = y*s;
		int oz = z*s;
		Vector3 corner1 = new Vector3(  ox,   oy,   oz);
		Vector3 corner2 = new Vector3(s+ox,   oy,   oz);
		Vector3 corner3 = new Vector3(s+ox, s+oy,   oz);
		Vector3 corner4 = new Vector3(  ox, s+oy,   oz);
		Vector3 corner5 = new Vector3(  ox,   oy, s+oz);
		Vector3 corner6 = new Vector3(s+ox,   oy, s+oz);
		Vector3 corner7 = new Vector3(s+ox, s+oy, s+oz);
		Vector3 corner8 = new Vector3(  ox, s+oy, s+oz);
		
		// check sides
		// +z
		if (block.sides[0].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner5, corner6, corner7, corner8, new Vector3(0, 0, -1));
		}
		// -z
		if (block.sides[1].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner4, corner3, corner2, corner1, new Vector3(0, 0, 1));
		}
		// +y
		if (block.sides[2].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner8, corner7, corner3, corner4, new Vector3(0, 1, 0));
		}
		// -y
		if (block.sides[3].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner6, corner5, corner1, corner2, new Vector3(0, -1, 0));
		}
		// +x
		if (block.sides[4].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner2, corner3, corner7, corner6, new Vector3(1, 0, 0));
		}
		// -x
		if (block.sides[5].isActive) {
			builder.part("side", GL10.GL_TRIANGLES, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner5, corner8, corner4, corner1, new Vector3(-1, 0, 0));
		}
	}
}
