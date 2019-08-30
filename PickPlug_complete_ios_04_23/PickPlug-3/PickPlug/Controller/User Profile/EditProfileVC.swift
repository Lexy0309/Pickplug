//
//  EditProfileVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage

class EditProfileVC: UIViewController,HeaderViewBackDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,ServiceApiResponseDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var imgProfile: UIImageView!
    @IBOutlet var viewImgHint: UIView!
    @IBOutlet var txtUserName: AITextField!
    @IBOutlet var txtEmail: AITextField!
    @IBOutlet var btnSave: AIButton!
    var imgFile = UIImage()
    @IBOutlet var outerImageView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = "Edit Profile"
        Utility.setPlaceholderColor(textField: txtUserName, placeholderText: "Enter Full Name", color: Utility.STRING_TEXT_COLOR_GREY)
        Utility.setPlaceholderColor(textField: txtEmail, placeholderText: "Enter Email Address", color: Utility.STRING_TEXT_COLOR_GREY)
        Utility.decorateButton(button: btnSave, borderColor: "", borderWidth: 0, cornerRadius: btnSave.frame.size.height / 2)
        outerImageView.dropShadowImageView()
        imgProfile.layer.cornerRadius = imgProfile.frame.size.width / 2
        txtUserName.text = Utility.readString(key: Utility.KEY_USER_NAME)
        txtEmail.text = Utility.readString(key: Utility.KEY_EMAIL)
        if !Utility.readString(key: Utility.KEY_USER_IMAGE).isEmpty{
            imgProfile.sd_setImage(with: URL(string:Utility.readString(key: Utility.KEY_USER_IMAGE)), placeholderImage: UIImage.init(named: Utility.USER_PLACEHOLDER), options: SDWebImageOptions.refreshCached, completed: nil)
            viewImgHint.isHidden = true
        }else{
            viewImgHint.isHidden = false
        }
        
    }
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func onClickImage(_ sender: Any) {
        self.selectImage()
    }
    @IBAction func onClickSave(_ sender: Any) {
        txtEmail.resignFirstResponder()
        txtUserName.resignFirstResponder()
        var errorCnt = 0
        var stringAlert = NSMutableString.init(string: "")
        if !Utility.isValidField(textField: txtUserName) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter your full name"
        }else if !Utility.isValidField(textField: txtEmail) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter email address"
        }else if !Utility.isValidEmail(email: txtEmail.text!) {
            errorCnt  = errorCnt+1
            stringAlert = "Enter valid email address"
        }
        if errorCnt > 0 {
            Utility.showSnackBar(message: stringAlert as String)
        }else{
            updateProfile()
        }
    }
    
    func updateProfile(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var parameter : [String:String]!
            parameter = ["fullname":txtUserName.text!,"email":txtEmail.text!,"user_id":Utility.readString(key: Utility.KEY_USER_ID)]
            Utility.sendRequestMultiPart(forUrl: Utility.UPDATE_USER_PROFILE, parameters: parameter, requestMethod: AIHTTPMethod.post, imgParameter: "photo", imgFileName: "image.jpg", imageFile: imgFile, withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
        if Utility.getStringSafely(strData: resultObj.object(forKey: "status")) == "success" {
            Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
            let modelObj = resultObj.object(forKey: "data") as! NSDictionary
            Utility.writeString(key: Utility.KEY_USER_ID, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Id")))
            Utility.writeString(key: Utility.KEY_USER_NAME, value: Utility.getStringSafely(strData: modelObj.object(forKey: "FullName")))
            Utility.writeString(key: Utility.KEY_EMAIL, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Email")))
            Utility.writeString(key: Utility.KEY_MOBILE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "PhoneNumber")))
            Utility.writeString(key: Utility.KEY_USER_IMAGE, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Photo")))
            self.navigationController?.popViewController(animated: true)
        }else{
            Utility.showSnackBar(message: Utility.getStringSafely(strData: resultObj.object(forKey: "message")))
        }
    }
    
    
    func selectImage(){
        let alert = UIAlertController(title: "Select Image", message: "", preferredStyle: .alert)
        let picker = UIImagePickerController()
        let camera = UIAlertAction(title: "Camera", style: .default, handler: { (action) -> Void in
            if UIImagePickerController.isSourceTypeAvailable(.camera) {
                picker.delegate = self
                picker.sourceType = .camera
                picker.cameraCaptureMode = .photo
                self.present(picker, animated: true, completion: nil)
            }
        })
        let gallery = UIAlertAction(title: "Gallery", style: .default, handler: { (action) -> Void in
            picker.allowsEditing = false
            picker.sourceType = .photoLibrary
            picker.delegate = self
            picker.mediaTypes = UIImagePickerController.availableMediaTypes(for: .photoLibrary)!
            self.present(picker, animated: true, completion: nil)
        })
        let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: { (action) -> Void in
            
        })
        alert.addAction(camera)
        alert.addAction(gallery)
        alert.addAction(cancel)
        self.present(alert, animated: true, completion: nil)
    }
    @objc func imagePickerController(_ picker: UIImagePickerController,
                                     didFinishPickingMediaWithInfo info: [String : Any]){
        let chosenImage = info[UIImagePickerControllerOriginalImage] as! UIImage
        imgProfile.image = chosenImage
        imgFile = chosenImage
        if imgProfile.image != nil{
            viewImgHint.isHidden = true
        }else{
            viewImgHint.isHidden = false
        }
        picker.dismiss(animated:true, completion: nil);
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
extension UIView {
    func dropShadowImageView(scale: Bool = true) {
        layer.masksToBounds = false
        layer.shadowOpacity = 0.8
        layer.shadowOffset = CGSize.zero
        layer.shadowRadius = 8
        layer.cornerRadius = self.frame.size.width / 2
        layer.shadowColor = Utility.hexStringToUIColor(hex: "#AAAAAA").cgColor
    }
}
