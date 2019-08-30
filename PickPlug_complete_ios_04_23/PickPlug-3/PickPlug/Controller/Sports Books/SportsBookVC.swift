//
//  SportsBookVC.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage

class SportsBookVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var dataTableView: UITableView!
    var arrData : NSArray = [["title":"BetOnline","imgLogo":"https://www.sportsbookpromocodes.com/wp-content/uploads/2017/01/sportsbookpromocodes-betonline-logo-1.png","description":"BetOnline ownership has invested in experienced wagering and customer service managers who understand that the sportsbook's first priority is service over sales. This attitude is passed along to the staff during training classes that each wagering clerk and service agent is required to pass. BetOnline treats players with respect and aims to gain a player's loyalty by providing VIP level service; including a generous sign-up and reload bonus","oprate_since":"2001","join_link":"https://record.commission.bz/_3E2mxfib6zVLcRLGwHoTKWNd7ZgqdRLk/1/"],["title":"Intertops","imgLogo":"http://www.pokerakademia.com/uploads/intertops-45347578.png","description":"Intertops is one of the oldest online sportsbooks around today having first opened its doors back in 1998. The sportsbook developed a reputation as being a reliable, old-fashioned betting site that paid on time and operated in an efficient, no frills manner. The sports betting site is typically mentioned at the top of most users must-have sportsbook accounts","oprate_since":"2002","join_link":"http://link.intertops.eu/c/394572"],["title":"Sportsbetting.ag","imgLogo":"https://388803b8fbeaf8c08fc98fcd-yqjivnwq09blzymkn6q.netdna-ssl.com/wp-content/uploads/2018/07/SportsBetting-ag-logo.png","description":"Sportsbetting has a competitive bonus program, a leading mobile betting platform, and typically scores highly in the customer service and speed of payout categories. User feedback has been consistently positive. Sportsbetting.ag offers a live betting platform.","oprate_since":"2002","join_link":"https://record.commission.bz/_3E2mxfib6zWMT4LEHD22I2Nd7ZgqdRLk/1/"]]
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = "Sport Books"
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 100
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        print("arrData",arrData,"=====",arrData.count)
        dataTableView.reloadData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func onClickBack(sender: Any) {
        self.tabBarController?.navigationController?.popViewController(animated: true)
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "SportsBookCell"
        var cell:SportsBookCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? SportsBookCell
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? SportsBookCell)!
        }
        
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        cell.imgProfile.sd_setImage(with: URL(string: Utility.getStringSafely(strData: modelObj.object(forKey: "imgLogo"))), placeholderImage: UIImage.init(named: ""), options: SDWebImageOptions.refreshCached, completed: nil)
        cell.lblTitle.text = Utility.getStringSafely(strData: modelObj.object(forKey: "title"))
        cell.lblOperateSince.text = "Operating since :" + Utility.getStringSafely(strData: modelObj.object(forKey: "oprate_since"))
        cell.lblDescription.text = Utility.getStringSafely(strData: modelObj.object(forKey: "description"))
        cell.btnJoin.tag =  indexPath.row
        cell.btnJoin.addTarget(self, action: #selector(onClickJoin(sender:)), for: .touchUpInside)
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
    }
    @objc func onClickJoin(sender: UIButton) {
        let modelObj = arrData.object(at: sender.tag) as! NSDictionary
        if let url = URL(string: Utility.getStringSafely(strData: modelObj.object(forKey: "join_link"))) {
            if #available(iOS 10, *){
                UIApplication.shared.open(url)
            }else{
                UIApplication.shared.openURL(url)
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
