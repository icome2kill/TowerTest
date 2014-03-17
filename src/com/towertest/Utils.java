package com.towertest;

import java.io.InputStream;
import java.io.OutputStream;

import com.towertest.managers.ResourceManager;

public class Utils {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================
	public static int getColFromX(float pX) {
		return (int) Math.floor(pX / ResourceManager.getInstance().tmxTiledMap.getTileWidth());
	}

	public static int getRowFromY(float pY) {
		return (int) Math.floor(pY / ResourceManager.getInstance().tmxTiledMap.getTileHeight());
	}

	public static float getXFromCol(int pC) {
		return Math.round(pC * ResourceManager.getInstance().tmxTiledMap.getTileWidth());
	}

	public static float getYFromRow(int pR) {
		return Math.round(pR * ResourceManager.getInstance().tmxTiledMap.getTileHeight());
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
