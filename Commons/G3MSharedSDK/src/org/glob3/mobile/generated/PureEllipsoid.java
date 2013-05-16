package org.glob3.mobile.generated; 
//
//  PureEllipsoid.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  PureEllipsoid.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




public class PureEllipsoid extends Ellipsoid
{
  private final Vector3D _radii ;
  private final Vector3D _radiiSquared ;
  private final Vector3D _radiiToTheFourth ;
  private final Vector3D _oneOverRadiiSquared ;



  public PureEllipsoid(Vector3D radii)
  {
     _radii = new Vector3D(radii);
     _radiiSquared = new Vector3D(new Vector3D(radii._x * radii._x, radii._y * radii._y, radii._z * radii._z));
     _radiiToTheFourth = new Vector3D(new Vector3D(_radiiSquared._x * _radiiSquared._x, _radiiSquared._y * _radiiSquared._y, _radiiSquared._z * _radiiSquared._z));
     _oneOverRadiiSquared = new Vector3D(new Vector3D(1.0 / (radii._x * radii._x), 1.0 / (radii._y * radii._y), 1.0 / (radii._z * radii._z)));
  
  }

  public void dispose()
  {

  }

  public final Vector3D getRadii()
  {
    return _radii;
  }

  public final Vector3D centricSurfaceNormal(Vector3D positionOnEllipsoid)
  {
    return positionOnEllipsoid.normalized();
  }

  public final Vector3D geodeticSurfaceNormal(Vector3D positionOnEllipsoid)
  {
    return positionOnEllipsoid.times(_oneOverRadiiSquared).normalized();
  }

  public final Vector3D geodeticSurfaceNormal(MutableVector3D positionOnEllipsoid)
  {
    return positionOnEllipsoid.times(_oneOverRadiiSquared).normalized().asVector3D();
  }


  public final Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude)
  {
    final double cosLatitude = latitude.cosinus();
  
    return new Vector3D(cosLatitude * longitude.cosinus(), cosLatitude * longitude.sinus(), latitude.sinus());
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
    return geodeticSurfaceNormal(geodetic.latitude(), geodetic.longitude());
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
    return geodeticSurfaceNormal(geodetic.latitude(), geodetic.longitude());
  }

  public final java.util.ArrayList<Double> intersectionsDistances(Vector3D origin, Vector3D direction)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // By laborious algebraic manipulation....
    final double a = (direction._x * direction._x * _oneOverRadiiSquared._x + direction._y * direction._y * _oneOverRadiiSquared._y + direction._z * direction._z * _oneOverRadiiSquared._z);
  
    final double b = 2.0 * (origin._x * direction._x * _oneOverRadiiSquared._x + origin._y * direction._y * _oneOverRadiiSquared._y + origin._z * direction._z * _oneOverRadiiSquared._z);
  
    final double c = (origin._x * origin._x * _oneOverRadiiSquared._x + origin._y * origin._y * _oneOverRadiiSquared._y + origin._z * origin._z * _oneOverRadiiSquared._z - 1.0);
  
    // Solve the quadratic equation: ax^2 + bx + c = 0.
    // Algorithm is from Wikipedia's "Quadratic equation" topic, and Wikipedia credits
    // Numerical Recipes in C, section 5.6: "Quadratic and Cubic Equations"
    final double discriminant = b * b - 4 * a * c;
    if (discriminant < 0.0)
    {
      // no intersections
      return intersections;
    }
    else if (discriminant == 0.0)
    {
      // one intersection at a tangent point
      //return new double[1] { -0.5 * b / a };
      intersections.add(-0.5 * b / a);
      return intersections;
    }
  
    final double t = -0.5 * (b + (b > 0.0 ? 1.0 : -1.0) * IMathUtils.instance().sqrt(discriminant));
    final double root1 = t / a;
    final double root2 = c / t;
  
    // Two intersections - return the smallest first.
    if (root1 < root2)
    {
      intersections.add(root1);
      intersections.add(root2);
    }
    else
    {
      intersections.add(root2);
      intersections.add(root1);
    }
    return intersections;
  }

  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
    final Vector3D n = geodeticSurfaceNormal(latitude, longitude);
  
    final Vector3D k = _radiiSquared.times(n);
    final double gamma = IMathUtils.instance().sqrt((k._x * n._x) + (k._y * n._y) + (k._z * n._z));
  
    final Vector3D rSurface = k.div(gamma);
    return rSurface.add(n.times(height));
  }

  public final Vector3D toCartesian(Geodetic3D geodetic)
  {
    return toCartesian(geodetic.latitude(), geodetic.longitude(), geodetic.height());
  }

  public final Vector3D toCartesian(Geodetic2D geodetic)
  {
    return toCartesian(geodetic.latitude(), geodetic.longitude(), 0.0);
  }

  public final Vector3D toCartesian(Geodetic2D geodetic, double height)
  {
    return toCartesian(geodetic.latitude(), geodetic.longitude(), height);
  }

  public final Geodetic2D toGeodetic2D(Vector3D positionOnEllipsoid)
  {
    final Vector3D n = geodeticSurfaceNormal(positionOnEllipsoid);
  
    final IMathUtils mu = IMathUtils.instance();
    return new Geodetic2D(Angle.fromRadians(mu.asin(n._z)), Angle.fromRadians(mu.atan2(n._y, n._x)));
  }

  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
    final Vector3D p = scaleToGeodeticSurface(position);
    final Vector3D h = position.sub(p);
  
    final double height = (h.dot(position) < 0) ? -1 * h.length() : h.length();
  
    return new Geodetic3D(toGeodetic2D(p), height);
  }

  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final double beta = 1.0 / mu.sqrt((position._x * position._x) * _oneOverRadiiSquared._x + (position._y * position._y) * _oneOverRadiiSquared._y + (position._z * position._z) * _oneOverRadiiSquared._z);
  
    final double n = new Vector3D(beta * position._x * _oneOverRadiiSquared._x, beta * position._y * _oneOverRadiiSquared._y, beta * position._z * _oneOverRadiiSquared._z).length();
  
    double alpha = (1.0 - beta) * (position.length() / n);
  
    final double x2 = position._x * position._x;
    final double y2 = position._y * position._y;
    final double z2 = position._z * position._z;
  
    double da = 0.0;
    double db = 0.0;
    double dc = 0.0;
  
    double s = 0.0;
    double dSdA = 1.0;
  
    do
    {
      alpha -= (s / dSdA);
  
      da = 1.0 + (alpha * _oneOverRadiiSquared._x);
      db = 1.0 + (alpha * _oneOverRadiiSquared._y);
      dc = 1.0 + (alpha * _oneOverRadiiSquared._z);
  
      final double da2 = da * da;
      final double db2 = db * db;
      final double dc2 = dc * dc;
  
      final double da3 = da * da2;
      final double db3 = db * db2;
      final double dc3 = dc * dc2;
  
      s = (x2 / (_radiiSquared._x * da2) + y2 / (_radiiSquared._y * db2) + z2 / (_radiiSquared._z * dc2) - 1.0);
  
      dSdA = (-2.0 * (x2 / (_radiiToTheFourth._x * da3) + y2 / (_radiiToTheFourth._y * db3) + z2 / (_radiiToTheFourth._z * dc3)));
    }
    while (mu.abs(s) > 1e-10);
  
    return new Vector3D(position._x / da, position._y / db, position._z / dc);
  }

  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
    final double beta = 1.0 / IMathUtils.instance().sqrt((position._x * position._x) * _oneOverRadiiSquared._x + (position._y * position._y) * _oneOverRadiiSquared._y + (position._z * position._z) * _oneOverRadiiSquared._z);
  
    return position.times(beta);
  }

  public final java.util.LinkedList<Vector3D> computeCurve(Vector3D start, Vector3D stop, double granularity)
  {
    if (granularity <= 0.0)
    {
      //throw new ArgumentOutOfRangeException("granularity", "Granularity must be greater than zero.");
      return new java.util.LinkedList<Vector3D>();
    }
  
    final Vector3D normal = start.cross(stop).normalized();
    final double theta = start.angleBetween(stop)._radians;
  
    //int n = max((int)(theta / granularity) - 1, 0);
    int n = ((int)(theta / granularity) - 1) > 0 ? (int)(theta / granularity) - 1 : 0;
  
    java.util.LinkedList<Vector3D> positions = new java.util.LinkedList<Vector3D>();
  
    positions.addLast(start);
  
    for (int i = 1; i <= n; ++i)
    {
      double phi = (i * granularity);
  
      positions.addLast(scaleToGeocentricSurface(start.rotateAroundAxis(normal, Angle.fromRadians(phi))));
    }
  
    positions.addLast(stop);
  
    return positions;
  }

  public final Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1)
  {
    final Vector3D v0 = toCartesian(P0);
    final Vector3D v1 = toCartesian(P1);
    final Vector3D normal = v0.cross(v1).normalized();
    final Angle theta = v0.angleBetween(v1);
    final Vector3D midPoint = scaleToGeocentricSurface(v0.rotateAroundAxis(normal, theta.times(0.5)));
    return toGeodetic2D(midPoint);
  }



  // compute distance from two points
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector3D radius = _radii;
    final double R = (radius._x + radius._y + radius._z) / 3;
  
    // spheric distance from P to Q
    // this is the right form, but it's the most complex
    // theres is a minimum error considering sphere instead of ellipsoid
    final double latP = g2.latitude()._radians;
    final double lonP = g2.longitude()._radians;
    final double latQ = g1.latitude()._radians;
    final double lonQ = g1.longitude()._radians;
    final double coslatP = mu.cos(latP);
    final double sinlatP = mu.sin(latP);
    final double coslonP = mu.cos(lonP);
    final double sinlonP = mu.sin(lonP);
    final double coslatQ = mu.cos(latQ);
    final double sinlatQ = mu.sin(latQ);
    final double coslonQ = mu.cos(lonQ);
    final double sinlonQ = mu.sin(lonQ);
    final double pq = (coslatP * sinlonP * coslatQ * sinlonQ + sinlatP * sinlatQ + coslatP * coslonP * coslatQ * coslonQ);
    return mu.acos(pq) * R;
  }


  // compute distance from two points
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final Vector3D radius = _radii;
    final double R = (radius._x + radius._y + radius._z) / 3;
  
    final double medLat = g1.latitude()._degrees;
    final double medLon = g1.longitude()._degrees;
  
    // this way is faster, and works properly further away from the poles
    //double diflat = fabs(g.latitude()-medLat);
    double diflat = mu.abs(g2.latitude()._degrees - medLat);
    if (diflat > 180)
    {
      diflat = 360 - diflat;
    }
  
    double diflon = mu.abs(g2.longitude()._degrees - medLon);
    if (diflon > 180)
    {
      diflon = 360 - diflon;
    }
  
    double dist = mu.sqrt(diflat * diflat + diflon * diflon);
    return dist * mu.pi() / 180 * R;
  }

  public final Vector3D closestPointToSphere(Vector3D pos, Vector3D ray)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    double t = 0;
  
    // compute radius for the rotation
    final double R0 = (_radii._x + _radii._y + _radii._y) /3;
  
    // compute the point in this ray that are to a distance R from the origin.
    final double U2 = ray.squaredLength();
    final double O2 = pos.squaredLength();
    final double OU = pos.dot(ray);
    final double a = U2;
    final double b = 2 * OU;
    final double c = O2 - R0 * R0;
    double rad = b * b - 4 * a * c;
  
    // if there is solution, the ray intersects the sphere
    if (rad > 0)
    {
      // compute the final point (the smaller positive t value)
      t = (-b - mu.sqrt(rad)) / (2 * a);
      if (t < 1)
         t = (-b + mu.sqrt(rad)) / (2 * a);
      // if the ideal ray intersects, but not the mesh --> case 2
      if (t < 1)
         rad = -12345;
    }
  
    // if no solution found, find a point in the contour line
    if (rad < 0)
    {
      final double D = mu.sqrt(O2);
      final double co2 = R0 * R0 / (D * D);
      final double a_ = OU * OU - co2 * O2 * U2;
      final double b_ = 2 * OU * O2 - co2 * 2 * OU * O2;
      final double c_ = O2 * O2 - co2 * O2 * O2;
      final double rad_ = b_ * b_ - 4 * a_ * c_;
      t = (-b_ - mu.sqrt(rad_)) / (2 * a_);
    }
  
    // compute the final point
    Vector3D result = pos.add(ray.times(t));
    return result;
  }

  public final Vector3D closestIntersection(Vector3D pos, Vector3D ray)
  {
    java.util.ArrayList<Double> distances = intersectionsDistances(pos, ray);
    if (distances.isEmpty())
    {
      return Vector3D.nan();
    }
    return pos.add(ray.times(distances.get(0)));
  }


  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
    final MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(toCartesian(position));
    final MutableMatrix44D rotation = MutableMatrix44D.createGeodeticRotationMatrix(position);
  
    return translation.multiply(rotation);
  }

}