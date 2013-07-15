package org.glob3.mobile.generated; 
///////////////////////////////////////////////////////////////////////////////////////////

public class GLCameraGroupFeature extends GLFeature
{
  private Matrix44DHolder _matrixHolder = null;
  public GLCameraGroupFeature(Matrix44D matrix)
  {
     super(GLFeatureGroupName.CAMERA_GROUP);
     _matrixHolder = matrix;
  }
  public void dispose()
  {
  }
  public final Matrix44D getMatrix()
  {
     return _matrixHolder.getMatrix();
  }
  public final void setMatrix(Matrix44D matrix)
  {
     _matrixHolder.setMatrix(matrix);
  }
  public final Matrix44DHolder getMatrixHolder()
  {
     return _matrixHolder;
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
  }
}