//
//  ChangePassVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class ChangePassVC: UIViewController,HeaderViewBackDelegate,ServiceApiResponseDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var txtCurrentPass: AITextField!
    @IBOutlet var txtNewPass: AITextField!
    @IBOutlet var txtConfirmPass: AITextField!
    @IBOutlet var btnSave: AIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = "Change Password"
        Utility.setPlaceholderColor(textField: txtCurrentPass, placeholderText: "Current Password", color: Utility.STRING_TEXT_COLOR_GREY)
        Utility.setPlaceholderColor(textField: txtNewPass, placeholderText: "New Password", color: Utility.STRING_TEXT_COLOR_GREY)
        Utility.setPlaceholderColor(textField: txtConfirmPass, placeholderText: "Confirm Password", color: Utility.STRING_TEXT_COLOR_GREY)
        Utility.decorateButton(button: btnSave, borderColor: "", borderWidth: 0, cornerRadius: btnSave.frame.size.height / 2)
        
    }
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @IBAction func onClickSave(_ sender: Any) {
        txtConfirmPass.resignFirstResponder()
        txtNewPass.resignFirstResponder()
        txtCurrentPass.resignFirstResponder()
        
        var errorCnt = 0
        var stringAlert = NSMutableString.init(string: "")
        if !Utility.isValidField(textField: txtCurrentPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your current password"
        }else if txtCurrentPass.text != Utility.readString(key: Utility.KEY_PASSWORD) {
            errorCnt  = errorCnt+1
            stringAlert = "Your current password does not match"
        }else if !Utility.isValidField(textField: txtNewPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your new password"
        }else if !Utility.isValidField(textField: txtConfirmPass) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your confirm password"
        }else if (txtNewPass.text != txtConfirmPass.text){
            errorCnt  = errorCnt+1
            stringAlert = "Your new password and confirm password does not match"
        }
        if errorCnt > 0 {
            Utility.showSnackBar(message: stringAlert as String)
        }else{
            updatePassword()
        }
    }
    func updatePassword(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var parameter : [String:String]!
            parameter = ["old_password":txtCurrentPass.text!,"new_password":txtNewPass.text!,"user_id":Utility.readString(key: Utility.KEY_USER_ID)]
            Utility.sendRequest(forUrl: Utility.CHANGE_PASSWORD, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
        if Utility.getStringSafely(strData: resultObj.object(forKey: "status")) == "success" {
            if txtNewPass.text != Utility.readString(key: Utility.KEY_PASSWORD){
                Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
                Utility.clearUserDefaults()
                let loginVC = self.storyboard?.instantiateViewController(withIdentifier: "Login_VC") as! LoginVC
                let arrCont:NSArray = NSArray.init(array: [loginVC])
                self.navigationController?.setViewControllers([arrCont [0] as! UIViewController], animated: true)
            }else{
                Utility.showSnackBar(message: "Password update successfully")
            }
            
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
