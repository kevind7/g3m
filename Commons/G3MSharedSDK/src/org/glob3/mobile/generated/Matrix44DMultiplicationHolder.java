package org.glob3.mobile.generated; 
public class Matrix44DMultiplicationHolder extends Matrix44DProvider
{
  private final Matrix44D[] _matrix;
  private final Matrix44DProvider[] _providers;
  private int _nMatrix;
  private Matrix44D _modelview;

  private void pullMatrixes()
  {
    for (int j = 0; j < _nMatrix; j++)
    {
      final Matrix44D newMatrix = _providers[j].getMatrix();
  
      if (newMatrix != _matrix[j])
      {
        if (_matrix[j] != null)
        {
          _matrix[j]._release();
        }
  
        _matrix[j] = newMatrix;
        _matrix[j]._retain();
      }
    }
  }

  public void dispose()
  {
  
    for (int j = 0; j < _nMatrix; j++)
    {
      if (_matrix[j] != null)
      {
        _matrix[j]._release();
      }
      _providers[j]._release();
    }
  
  
    if (_modelview != null)
    {
      _modelview._release();
    }
    {
  
      super.dispose();
    }
  }

  public Matrix44DMultiplicationHolder(Matrix44DProvider[] providers, int nMatrix)
  {
     _nMatrix = nMatrix;
     _modelview = null;
    _matrix = new Matrix44D[nMatrix];
    _providers = new Matrix44DProvider[nMatrix];
    for (int i = 0; i < _nMatrix; i++)
    {
      _matrix[i] = null;
      _providers[i] = providers[i];
      _providers[i]._retain();
    }
  
    pullMatrixes();
  }

  public final Matrix44D getMatrix()
  {
  
    if (_modelview != null)
    {
      for (int i = 0; i < _nMatrix; i++)
      {
        final Matrix44D m = _providers[i].getMatrix();
        if (m == null)
        {
          ILogger.instance().logError("Modelview multiplication failure");
        }
  
        if (_matrix[i] != m)
        {
          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview._release(); //NEW MODELVIEW NEEDED
          _modelview = null;
  
          pullMatrixes();
          break;
        }
      }
    }
  
    if (_modelview == null)
    {
      _modelview = new Matrix44D(_matrix[0]);
      for (int i = 1; i < _nMatrix; i++)
      {
        final Matrix44D m2 = _matrix[i];
        Matrix44D m3 = _modelview.createMultiplication(m2);
        _modelview._release();
        _modelview = m3;
      }
    }
    return _modelview;
  }

}