//
//  HomeVC.swift
//  PickPlug
//
//  Created by abhinav on 25/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage

class HomeVC: UIViewController,HeaderViewDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate {
    
    @IBOutlet var headerView: HeaderView!
    @IBOutlet var viewMyProfile: UIView!
    @IBOutlet var lblMyProfile: AILabel!
    @IBOutlet var lblSubscription: AILabel!
    @IBOutlet var viewFreePicks: UIView!
    @IBOutlet var lblFreePick: AILabel!
    @IBOutlet var lblFreePickCount: AILabel!
    @IBOutlet var viewTableData: UIView!
    @IBOutlet var lblPremiumPicks: AILabel!
    @IBOutlet var dataTableView: UITableView!
    @IBOutlet var viewTableHeightCons: NSLayoutConstraint!
    var arrData = NSMutableArray.init()
    var arrPickDetail = NSMutableArray.init()
    
    @IBOutlet var imgProfile: UIImageView!
    var arrUserSubscription = NSArray.init()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.delegate = self
        headerView.viewMenu.isHidden = false
        headerView.viewBack.isHidden = true
        headerView.viewNotify.isHidden = true
        headerView.lblHeader.isHidden = true
        headerView.imgTopLogo.isHidden = false
        headerView.lblHeader.text = "Home"
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 65
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        viewMyProfile.dropShadow()
        viewTableData.dropShadow()
        Utility.decorateLabel(label: lblFreePickCount, borderColor: "#e0e0e0", borderWidth: 1, cornerRadius: lblFreePickCount.frame.size.width / 2)
        viewFreePicks.dropShadowHome()
        //self.getSportsDetails()
        imgProfile.layer.cornerRadius = imgProfile.frame.size.width / 2
        
        lblSubscription.text = "You have "+String(0) + " active subscriptions"
        (UIApplication.shared.delegate as! AppDelegate).currentController = self
        
        NotificationCenter.default.addObserver(self, selector: #selector(getSportsDetails),
                                               name: .UIApplicationWillEnterForeground,
                                               object: nil)
        
    }
    override func viewDidAppear(_ animated: Bool) {
        imgProfile.sd_setImage(with: URL(string:Utility.readString(key: Utility.KEY_USER_IMAGE)), placeholderImage: UIImage.init(named: Utility.USER_PLACEHOLDER), options: SDWebImageOptions.refreshCached, completed: nil)
        
        viewTableHeightCons.constant = dataTableView.contentSize.height + 65
        if Utility.readString(key: Utility.KEY_LOGOUT) == "yes"{
             Utility.writeString(key: Utility.KEY_LOGOUT, value: "no")
            self.callLogout()
        }else{
            if Utility.readString(key: Utility.KEY_SIDE_MENU_OPEN) != "yes" {
                Utility.writeString(key: Utility.KEY_IS_PURCHASED, value: "no")
                self.getSportsDetails()
            }
            
        }
        Utility.writeString(key: Utility.KEY_SIDE_MENU_OPEN, value: "no")
    }
    
    func onClickMenu(sender: Any) {
        Utility.writeString(key: Utility.KEY_SIDE_MENU_OPEN, value: "yes")
        let controller =  self.navigationController?.parent as! KYDrawerController
        controller.setDrawerState(.opened, animated: true)
    }
    
    func onClickSearch(sender: Any) {
        
    }
    //61afbbb9666c2d5737c126097682ddbfbdc95bd5
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    
    @objc func getSportsDetails(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            arrUserSubscription =  NSArray.init()
            arrData = NSMutableArray.init()
            arrPickDetail = NSMutableArray.init()
            Utility.sendRequest(forUrl: Utility.GET_SPORTS + Utility.readString(key: Utility.KEY_USER_ID), parameters: [:], requestMethod: AIHTTPMethod.get, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = ((response as! NSDictionary).object(forKey: "Results") as! NSDictionary).object(forKey: "allsports") as! NSArray
        lblFreePickCount.text = Utility.getStringSafely(strData: ((response as! NSDictionary).object(forKey: "Results") as! NSDictionary).object(forKey: "freepicks"))
        let userSubs = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
        if userSubs.object(forKey: "user_subscription") is NSArray {
            arrUserSubscription = (userSubs.object(forKey: "user_subscription") as! NSArray).value(forKey: "Sport") as! NSArray            
        }
        print("arrUserSubscription",arrUserSubscription)
        
        if resultObj.count > 0 {
            arrData = resultObj.mutableCopy() as! NSMutableArray            
            dataTableView.reloadData()
            viewTableHeightCons.constant = dataTableView.contentSize.height + 65
        }
        
        if arrUserSubscription.contains("0") {
            lblSubscription.text = "You have "+String(arrData.count)+" active subscriptions"
        }else{
            lblSubscription.text = "You have "+String(arrUserSubscription.count)+" active subscriptions"
            
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "HomeCell"
        var cell:HomeCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? HomeCell
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? HomeCell)!
        }
        cell.lblTitle.text = Utility.getStringSafely(strData: modelObj.object(forKey: "SportName")) + " Picks"
        cell.lblCount.isHidden = true
        Utility.decorateLabel(label: cell.lblCount, borderColor: "#e0e0e0", borderWidth: 1, cornerRadius: cell.lblCount.frame.size.width / 2)
        
        if Utility.getStringSafely(strData: modelObj.object(forKey: "count")) != String(0) {
            cell.lblCount.isHidden = false
            cell.lblCount.text = Utility.getStringSafely(strData: modelObj.object(forKey: "count"))
        }else{
            cell.lblCount.isHidden = true
        }
        cell.imgTitle.sd_setImage(with: URL(string: Utility.getStringSafely(strData: modelObj.object(forKey: "SportIcon"))), completed: nil)
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        Utility.writeString(key: Utility.KEY_IS_TYPE_PICKS, value: Utility.getStringSafely(strData: modelObj.object(forKey: "SportName")) + "")
        self.getPcksDetails(strSportId: Utility.getStringSafely(strData: modelObj.object(forKey: "Id")))
        Utility.writeString(key: Utility.KEY_PICKS_SPORT_ID, value: Utility.getStringSafely(strData: modelObj.object(forKey: "Id")))
        Utility.writeString(key: Utility.KEY_PICKS_ICON_URL, value: Utility.getStringSafely(strData: modelObj.object(forKey: "SportIcon")))
    }

    
    @IBAction func onClickMyProfile(_ sender: Any) {
        let vc  = self.storyboard?.instantiateViewController(withIdentifier: "Subscription_VC") as! SubscriptionVC
        self.navigationController?.pushViewController(vc, animated: true)
        
        /*let vc = self.storyboard?.instantiateViewController(withIdentifier: "UserProfile_VC") as! UserProfileVC
        self.navigationController?.pushViewController(vc, animated: true)*/
    }
    @IBAction func onClickFreePicks(_ sender: Any) {
        Utility.writeString(key: Utility.KEY_IS_TYPE_PICKS, value: "Free")
        Utility.writeString(key: Utility.KEY_PICKS_SPORT_ID, value: "")
        Utility.writeString(key: Utility.KEY_PICKS_ICON_URL, value: "")
        getPcksDetails(strSportId:"")
        
    }
    func goToTab(modelDict:NSDictionary){
        (UIApplication.shared.delegate as! AppDelegate).modelObj = modelDict
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "Tab_VC") as! TabVC
        vc.tabBarController?.selectedIndex = 0
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    
    func getPcksDetails(strSportId:String){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var strUrl = ""
            var parameter : [String:String]!
            if Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) == "Free"{
                strUrl = Utility.GET_FREEPICKS
                parameter = [:]
            }else{
                parameter = ["user_id":Utility.readString(key: Utility.KEY_USER_ID),"sport_id":strSportId]
                strUrl = Utility.GET_PICK_BY_ID
            }
            let pickDetail = getPicksDetail.init()
            pickDetail.controller = self
            
            Utility.sendRequest(forUrl: strUrl, parameters: parameter, requestMethod: AIHTTPMethod.post, others: [:], withDelegate: pickDetail)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    class getPicksDetail : ServiceApiResponseDelegate {
        var controller : HomeVC!
        func serviceResponseCallBack(response: Any) {
            Utility.hideLoader()
            print("Response picks",response)
            let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSDictionary
            
            let allPickObj = resultObj.object(forKey: "allpicks") as! NSDictionary
            if allPickObj.count > 0 {
                controller.goToTab(modelDict: resultObj)
            }else{
                let vc = controller.storyboard?.instantiateViewController(withIdentifier: "Tab_VC") as! TabVC
                vc.tabBarController?.selectedIndex = 0
                vc.isPresent = "not_Available"
                controller.navigationController?.pushViewController(vc, animated: true)
                //let vc  = controller.storyboard?.instantiateViewController(withIdentifier: "ParleyPic_VC") as! ParleyPicVC
                //controller.navigationController?.pushViewController(vc, animated: true)
            }
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
    func dropShadowHome(scale: Bool = true) {
        layer.masksToBounds = false
        layer.shadowOpacity = 0.5
        layer.shadowOffset = CGSize.zero
        layer.shadowRadius = 5
        layer.cornerRadius = self.frame.size.height / 2
        layer.shadowColor = Utility.hexStringToUIColor(hex: "#AAAAAA").cgColor
    }
}
