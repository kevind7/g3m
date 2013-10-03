package org.glob3.mobile.generated; 
//
//  TerrainTouchEventListener.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/3/13.
//
//

//
//  TerrainTouchEventListener.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 14/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//class Layer;

public class TerrainTouchEvent
{
  private final Geodetic3D _position ;
  private final Sector _sector ;
  private final Layer _layer;

  public TerrainTouchEvent(Geodetic3D position, Sector sector, Layer layer)
  {
     _position = new Geodetic3D(position);
     _sector = new Sector(sector);
     _layer = layer;

  }

  public final Geodetic3D getPosition()
  {
    return _position;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final Layer getLayer()
  {
    return _layer;
  }

}