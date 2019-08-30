//
//  ForgotPassVC.swift
//  PickPlug
//
//  Created by abhinav on 26/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class ForgotPassVC: UIViewController,ServiceApiResponseDelegate {

    @IBOutlet var txtEmail: AITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        Utility.setPlaceholderColor(textField: txtEmail, placeholderText: "Email Address", color: Utility.STRING_TEXT_COLOR)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickBack(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func onClickSubmit(_ sender: Any) {
        txtEmail.resignFirstResponder()
        var errorCnt = 0
        var stringAlert = NSMutableString.init(string: "")
        
        if !Utility.isValidField(textField: txtEmail) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter email address"
        }else if !Utility.isValidEmail(email: txtEmail.text!) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter valid email address"
        }
        
        if errorCnt > 0 {
            Utility.showSnackBar(message: stringAlert as String)
        }else{
            forgotPassword()
        }
    }
    func forgotPassword(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var parameter : [String:String]!
            parameter = ["email":txtEmail.text!]
            Utility.sendRequest(forUrl: Utility.FORGOT_PASSWORD, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Forget") as! NSDictionary
        if Utility.getStringSafely(strData: resultObj.object(forKey: "status")) == "success" {
           Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
           self.navigationController?.popViewController(animated: true)
        }else{
            Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
        }
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
