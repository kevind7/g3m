//
//  G3MDemoScene.hpp
//  G3MApp
//
//  Created by Diego Gomez Deck on 11/16/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#ifndef __G3MApp__G3MDemoScene__
#define __G3MApp__G3MDemoScene__

#include <string>
#include <vector>

class G3MDemoModel;
class G3MContext;


class G3MDemoScene {
private:
  G3MDemoModel* _model;
  int _selectedOptionIndex;

  int getOptionIndex(const std::string& option) const;

protected:
  const std::string        _name;
  std::vector<std::string> _options;

  G3MDemoScene(const std::string& name,
               G3MDemoModel* model) :
  _name(name),
  _model(model)
  {
  }

  virtual void rawActivate(const G3MContext* context) = 0;

  virtual void rawSelectOption(const std::string& option,
                               int optionIndex) = 0;

public:

  virtual ~G3MDemoScene() {
  }

  const std::string getName() const {
    return _name;
  }

  G3MDemoModel* getModel() const {
    return _model;
  }

  const int getOptionsCount() const {
    return _options.size();
  }

  const std::string getOption(int index) const {
    return _options[index];
  }

  bool isSelectedOption(const std::string& option) const {
    return _options[_selectedOptionIndex] == option;
  }

  void selectOption(const std::string& option);

  void activate(const G3MContext* context);

  virtual void deactivate(const G3MContext* context);
  
};

#endif