//
//  SideMenuVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2017 abhinav. All rights reserved.
//

import UIKit
import SDWebImage
import FBSDKLoginKit
import MessageUI

class SideMenuVC: UIViewController,UITableViewDelegate,UITableViewDataSource,MFMailComposeViewControllerDelegate {

    @IBOutlet var imgProfile: UIImageView!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var viewProfile: UIView!
    @IBOutlet var btnLogout: AIButton!
    var arrItem = NSMutableArray()
    var arrItemSelect = NSMutableArray()
    var arrImage = NSMutableArray()
    var arrImageSelect = NSMutableArray()
    var selectedIndex = Int()
    var selectedSection = Int();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        selectedIndex = 0
        selectedSection = 0
        arrItem = ["HOME","MY PROFILE","ABOUT","RATE AND REVIEW","TERMS OF USE","PRIVACY POLICY","CONTACT US"]
        arrImage = ["img_menu_tick","img_menu_profile","img_menu_about","img_menu_rate","img_menu_legal","img_privacy_policy.png","img_contact_us.png"]
        tableView.delegate = self
        tableView.dataSource = self
        print("call SideMenu")
        // Do any additional setup after loading the view.
        
        imgProfile.layer.cornerRadius = imgProfile.frame.size.width / 2
        imgProfile.layer.borderWidth = 5
        imgProfile.layer.borderColor = Utility.hexStringToUIColor(hex: "#f7eb42").cgColor
        Utility.decorateView(view: viewProfile, borderColor: "", borderWidth: 0, cornerRadius: viewProfile.frame.size.width / 2)
        Utility.decorateButton(button: btnLogout, borderColor: "#ffffff", borderWidth: 1, cornerRadius: 8)
        tableView.tableFooterView = UIView.init()
        
        
    }
    override func viewDidAppear(_ animated: Bool) {
        imgProfile.sd_setImage(with: URL(string:Utility.readString(key: Utility.KEY_USER_IMAGE)), placeholderImage: UIImage.init(named: Utility.USER_PLACEHOLDER), options: SDWebImageOptions.refreshCached, completed: nil)
    }
   
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrItem.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:UITableViewCell = self.tableView.dequeueReusableCell(withIdentifier: "Cell") as UITableViewCell!
        let label:UILabel = cell.viewWithTag(101) as! UILabel
        let imgMenu :UIImageView = cell.viewWithTag(102) as! UIImageView
        label.text = arrItem.object(at: indexPath.row) as? String
        imgMenu.image = UIImage.init(named: arrImage.object(at: indexPath.row) as! String)
        
        
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("You tapped cell number \(indexPath.row).")
        selectedIndex = indexPath.row
        selectedSection = indexPath.section
       
        
        let elDrawer = self.navigationController?.parent as! KYDrawerController
        elDrawer.setDrawerState(.closed, animated: true)
        
        if (indexPath.row == 0) {
            /*let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "Home_VC") as! HomeVC
            mainNavigaion.pushViewController(vc, animated: true)*/
            
        }else if (indexPath.row == 1) {
            let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "UserProfile_VC") as! UserProfileVC
            mainNavigaion.pushViewController(vc, animated: true)
        }/*else if (indexPath.row == 2) {
            let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "Notification_VC") as! NotificationVC
            mainNavigaion.pushViewController(vc, animated: true)
        }*/else if (indexPath.row == 2) {
            let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "About_VC") as! AboutVC
            mainNavigaion.pushViewController(vc, animated: true)
        }else if (indexPath.row == 3) {
            if (UIApplication.shared.canOpenURL(NSURL(string:Utility.RATE_REVIEW_LINK)! as URL)){
                if #available(iOS 10.0, *) {
                    UIApplication.shared.open(NSURL(string:Utility.RATE_REVIEW_LINK)! as URL, options: [:], completionHandler: nil)
                } else {
                    // Fallback on earlier versions
                }
            }
        }else if (indexPath.row == 4) {
            let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
            vc.strHeader = "TERMS OF USE"
            mainNavigaion.pushViewController(vc, animated: true)
            
        }else if (indexPath.row == 5) {
            let storyboard: UIStoryboard = self.storyboard!
            let mainNavigaion = elDrawer.mainViewController as! UINavigationController
            let vc = storyboard.instantiateViewController(withIdentifier: "Legal_VC") as! LegalVC
            vc.strHeader = "PRIVACY POLICY"
            mainNavigaion.pushViewController(vc, animated: true)
            
        }else if (indexPath.row == 6) {
            let email = "picks@pickplug.com"
            if MFMailComposeViewController.canSendMail() {
                let mail = MFMailComposeViewController()
                mail.mailComposeDelegate = self
                mail.setToRecipients([email])
                mail.setMessageBody("", isHTML: true)
                self.present(mail, animated: true, completion: {
                    
                })
            } else {
                Utility.showSnackBar(message: "Please add mail account in settings.")
            }
            
        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 55
    }
    
    @IBAction func onClickLogout(_ sender: Any) {
        let elDrawer = self.navigationController?.parent as! KYDrawerController
        elDrawer.setDrawerState(.closed, animated: true)
        Utility.writeString(key: Utility.KEY_LOGOUT, value: "yes")
    }
    
    func mailComposeController(_ controller: MFMailComposeViewController, didFinishWith result: MFMailComposeResult, error: Error?) {
        controller.dismiss(animated: true)
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
extension UIViewController {
    func callLogout(){
        let refreshAlert = UIAlertController(title: "Alert!", message: "Are you sure want to logout?", preferredStyle: UIAlertControllerStyle.alert)
        refreshAlert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { (action: UIAlertAction!) in
            Utility.clearUserDefaults()
            if Utility.readString(key: Utility.KEY_IS_LOGIN_FACEBOOK) == "yes" {
                Utility.writeString(key: Utility.KEY_IS_LOGIN_FACEBOOK, value: "no")
                let loginManager = FBSDKLoginManager.init()
                loginManager.logOut()
            }
            let loginVC = self.storyboard?.instantiateViewController(withIdentifier: "Login_VC") as! LoginVC
            let arrCont:NSArray = NSArray.init(array: [loginVC])
            self.navigationController?.setViewControllers([arrCont [0] as! UIViewController], animated: true)
        }))
        refreshAlert.addAction(UIAlertAction(title: "No", style: .cancel, handler: { (action: UIAlertAction!) in
        }))
        present(refreshAlert, animated: true, completion: nil)
    }
}
