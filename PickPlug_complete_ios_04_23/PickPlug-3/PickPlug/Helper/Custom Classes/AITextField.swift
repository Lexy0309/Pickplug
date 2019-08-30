//
//  AITextField.swift
//  SMP
//
//  Created by abhinav on 05/10/17.
//  Copyright © 2017 ArchiveInfotech. All rights reserved.
//

import UIKit


class AITextField: UITextField {

    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
       // updateText(txtField: self)
        
    }
    
    func updateText(txtField:AITextField) {
        txtField.tintColor = Utility.hexStringToUIColor(hex: "#000000")
    }
    
  /*  let appTextColor: UIColor = UIColor(red: 0 / 255, green: 86 / 255, blue: 137 / 255, alpha: 1.0)
    
    
    /*
    // Only override draw() if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func draw(_ rect: CGRect) {
        // Drawing code
    }
    */

    required public init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        applySkyscannerTheme(textField: self)
        
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    func applySkyscannerTheme(textField: SkyFloatingLabelTextField) {
        
        textField.tintColor = appTextColor
        
        textField.textColor = appTextColor
        textField.lineColor = appTextColor
        
        textField.titleColor = appTextColor
        
        textField.placeholderColor = appTextColor;
        
        textField.selectedTitleColor = appTextColor
        textField.selectedLineColor = appTextColor
        
        // Set custom fonts for the title, placeholder and textfield labels
        textField.titleLabel.font = UIFont(name: "Ubuntu-Light", size: 12)
        textField.placeholderFont = UIFont(name: "Ubuntu-Light", size: 18)
        textField.font = UIFont(name: "Ubuntu-Light", size: 18)
        
        
    }
    
    func setAIFont(size:CGFloat,style:Int) { // 0 Regular, 1 Medium , 2 Bold
        /*titleLabel.font = UIFont(name: "Ubuntu-Light", size: 5)
        placeholderFont = UIFont(name: "Ubuntu-Light", size: 5)
        font = UIFont(name: "Ubuntu-Light", size: size)*/
        
        titleLabel.font = UIFont(name: "Ubuntu-Light", size: 12)
        placeholderFont = UIFont(name: "Ubuntu-Light", size: size)
        font = UIFont(name: "Ubuntu-Light", size: size)
        
        
    }*/
    
    
}
