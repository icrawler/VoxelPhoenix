package com.phoenix.voxel;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.materials.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * BlockFace Class
 * 
 * @author Phoenix Enero
 */
public class BlockFace {
	public int x;
	public int y;
	public int z;
	public int w;
	public int h;
	/**
	 * 0 - +z
	 * 1 - -z
	 * 2 - +y
	 * 3 - -y
	 * 4 - +x
	 * 5 - -x
	 */
	public int orientation;
	public int sideType;
	
	public BlockFace(int x, int y, int z, int w, int l, int orientation, int sideType) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		this.h = l;
		this.orientation = orientation;
		this.sideType = sideType;
	}
	
	public BlockFace(BlockFace copyFrom) {
		if (copyFrom != null) {
			this.w = copyFrom.w;
			this.h = copyFrom.h;
			this.x = copyFrom.x;
			this.y = copyFrom.y;
			this.z = copyFrom.z;
			this.orientation = copyFrom.orientation;
			this.sideType = copyFrom.sideType;
		}
	}
	
	public void createFace(ModelBuilder builder, int s) {
		Material currentMaterial = new Material(ColorAttribute.createDiffuse(BlockAttributes.Colors[sideType]));
		int ox = x*s;
		int oy = y*s;
		int oz = z*s;
		int sx = 1;
		int sy = 1;
		int sz = 1;
		if (orientation == 0 || orientation == 1) {
			sx = w;
			sy = h;
			sz = 1;
		}
		else if (orientation == 2 || orientation == 3) {
			sx = w;
			sy = 1;
			sz = h;
		}
		else if (orientation == 4 || orientation == 5) {
			sx = 1;
			sy = h;
			sz = w;
		}
		Vector3 corner1 = new Vector3(     ox,      oy,      oz);
		Vector3 corner2 = new Vector3(s*sx+ox,      oy,      oz);
		Vector3 corner3 = new Vector3(s*sx+ox, s*sy+oy,      oz);
		Vector3 corner4 = new Vector3(     ox, s*sy+oy,      oz);
		Vector3 corner5 = new Vector3(     ox,      oy, s*sz+oz);
		Vector3 corner6 = new Vector3(s*sx+ox,      oy, s*sz+oz);
		Vector3 corner7 = new Vector3(s*sx+ox, s*sy+oy, s*sz+oz);
		Vector3 corner8 = new Vector3(     ox, s*sy+oy, s*sz+oz);
		
		int mode = GL10.GL_TRIANGLES;
		
		// check sides
		// +z
		if (orientation == 0) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner5, corner6, corner7, corner8, new Vector3(0, 0, 1));
		}
		// -z
		if (orientation == 1) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner4, corner3, corner2, corner1, new Vector3(0, 0, -1));
		}
		// +y
		if (orientation == 2) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner8, corner7, corner3, corner4, new Vector3(0, 1, 0));
		}
		// -y
		if (orientation == 3) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner6, corner5, corner1, corner2, new Vector3(0, -1, 0));
		}
		// +x
		if (orientation == 4) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner2, corner3, corner7, corner6, new Vector3(1, 0, 0));
		}
		// -x
		if (orientation == 5) {
			builder.part("side", mode, Usage.Position | Usage.Normal, currentMaterial)
			.rect(corner5, corner8, corner4, corner1, new Vector3(-1, 0, 0));
		}
	}

}
