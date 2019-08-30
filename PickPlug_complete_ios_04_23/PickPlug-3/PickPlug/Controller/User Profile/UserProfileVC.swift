//
//  UserProfileVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage

class UserProfileVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate {
    
    @IBOutlet var headerView: HeaderView!
    @IBOutlet var imgProfile: UIImageView!
    @IBOutlet var lblUserName: AILabel!
    @IBOutlet var lblEmail: AILabel!
    @IBOutlet var viewTable: UIView!
    @IBOutlet var dataTableView: UITableView!
    @IBOutlet var viewEditProfile: UIView!
    @IBOutlet var viewChangePass: UIView!
    var arrData = NSMutableArray.init()
    @IBOutlet var tableViewHeightCons: NSLayoutConstraint!
    @IBOutlet weak var lblNoRecordFound: UILabel!
    var arrUserPicks = NSMutableArray.init()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = "My Profile"
        viewTable.dropShadow()
        viewEditProfile.dropShadow()
        viewChangePass.dropShadow()
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 75
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        imgProfile.layer.cornerRadius = imgProfile.frame.size.width / 2
        imgProfile.layer.borderWidth = 5
        imgProfile.layer.borderColor = Utility.hexStringToUIColor(hex: "#f7eb42").cgColor
        
        getUserSubscription()
        
    }
    
    func getUserSubscription(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            Utility.sendRequest(forUrl: Utility.GET_USER_SUBSCRIPTION, parameters: ["user_id":Utility.readString(key: Utility.KEY_USER_ID)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        if (response as! NSDictionary).object(forKey: "Results") is NSArray {
            let results = (response as! NSDictionary).object(forKey: "Results") as! NSArray
            lblNoRecordFound.isHidden =  true
            dataTableView.isHidden = false
            arrUserPicks.addObjects(from: results as! [Any])
            dataTableView.reloadData()
            tableViewHeightCons.constant =  CGFloat(arrUserPicks.count * 100)
        }else{
            lblNoRecordFound.isHidden =  false
            dataTableView.isHidden = true
            tableViewHeightCons.constant = 80
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        lblUserName.text = Utility.readString(key: Utility.KEY_USER_NAME)
        lblEmail.text = Utility.readString(key: Utility.KEY_EMAIL)
        imgProfile.sd_setImage(with: URL(string:Utility.readString(key: Utility.KEY_USER_IMAGE)), placeholderImage: UIImage.init(named: Utility.USER_PLACEHOLDER), options: SDWebImageOptions.refreshCached, completed: nil)
    }
    func onClickBack(sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrUserPicks.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let modelObj = arrUserPicks.object(at: indexPath.row) as! NSDictionary
        let cellIdentifier = "ProfileCell"
        var cell:ProfileCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? ProfileCell
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? ProfileCell)!
        }
        
        if modelObj.object(forKey: "SportDetail") is String {
            cell.imgTitleImage.isHidden = true
            cell.lblTitle.text =  Utility.getStringSafely(strData: modelObj.object(forKey: "Name"))
        }else{
            cell.imgTitleImage.isHidden = false
            let sportObj =  (modelObj.object(forKey: "SportDetail") as! NSArray).object(at: 0) as! NSDictionary
            cell.lblTitle.text =  Utility.getStringSafely(strData: sportObj.object(forKey: "SportName"))+" Picks"
            let imageName = Utility.getStringSafely(strData: modelObj.object(forKey: "sportIconImgPrefix")) + Utility.getStringSafely(strData: sportObj.object(forKey: "SportIcon"))
            cell.imgTitleImage!.sd_setImage(with: URL.init(string: imageName), placeholderImage: nil, options: .continueInBackground, completed: nil)
        }
        
        cell.lblDate.text =  Utility.getStringSafely(strData: modelObj.object(forKey: "ExpiryDate"))
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func onClickEditProfile(_ sender: UIButton) {
        if sender.tag == 101{
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "EditProfile_VC") as! EditProfileVC
            self.navigationController?.pushViewController(vc, animated: true)
        }else{
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "ChangePass_VC") as! ChangePassVC
            self.navigationController?.pushViewController(vc, animated: true)
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
extension UIView {
    func dropShadow(scale: Bool = true) {
        layer.masksToBounds = false
        layer.shadowOpacity = 0.6
        layer.shadowOffset = CGSize.zero
        layer.shadowRadius = 8
        layer.cornerRadius = 10
        layer.shadowColor = Utility.hexStringToUIColor(hex: "#AAAAAA").cgColor
    }
}

