//
//  NHLRecordsVC.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class NHLRecordsVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var lblNHLRecords: AILabel!
    @IBOutlet var dataTableView: UITableView!
    @IBOutlet var viewTop: UIView!
    @IBOutlet var lblCountPics: AILabel!
    var strNHLId = ""
    var arrData = NSMutableArray.init()
    @IBOutlet var lblNoRecord: AILabel!
    
    @IBOutlet weak var lblRecordHeader: AILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) + " Records"
        
        lblRecordHeader.text = Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) + " Records"
        
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 102
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        viewTop.dropShadow()
       // let modelObj = (UIApplication.shared.delegate as! AppDelegate).modelObj
        strNHLId = Utility.readString(key: Utility.KEY_PICKS_SPORT_ID)
        
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func viewDidAppear(_ animated: Bool) {
        self.getSportsDetails()
    }
    func getSportsDetails(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            Utility.sendRequest(forUrl: Utility.GET_NHL_SPORTS + strNHLId, parameters: [:], requestMethod: AIHTTPMethod.get, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = (response as! NSDictionary).object(forKey: "Picks") as! NSDictionary
        lblNHLRecords.text = Utility.getStringSafely(strData: (((resultObj.object(forKey: "SportDetails") as! NSArray).object(at: 0)as! NSDictionary).object(forKey: "RecordNo")))
        if (resultObj.object(forKey: "PickDetails") as! NSArray).count > 0 {
            arrData = (resultObj.object(forKey: "PickDetails") as! NSArray).mutableCopy() as! NSMutableArray
            if !arrData.contains("No records found") {
                lblCountPics.text = "Last " + String(arrData.count) + " Picks"
                dataTableView.isHidden = false
                lblCountPics.isHidden = false
               // viewTop.isHidden = false
                self.dataTableView.reloadData()
                lblNoRecord.isHidden = true
            }else{
               // viewTop.isHidden = true
                lblNoRecord.isHidden = false
                dataTableView.isHidden = true
                 lblCountPics.text = "No Picks"
                
            }
            
        }
        
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
        let cellIdentifier = "NHLRecordsCell"
        var cell:NHLRecordsCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? NHLRecordsCell
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? NHLRecordsCell)!
        }
        
        if (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).count > 0{
            cell.lblPickSecond.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgPickSecond.sd_setImage(with: URL(string:Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }
        if (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).count > 0{
            cell.lblPicksFirst.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgPicksFirst.sd_setImage(with: URL(string:Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }
        cell.lblDate.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "PickdateNew")), withFormat: "d", enterFormat: "dd-MM-yyyy")
        cell.lblMonth.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "PickdateNew")), withFormat: "MMMM", enterFormat: "dd-MM-yyyy")
        if Utility.getStringSafely(strData: modelObj.object(forKey: "PickStatus")) == "Lose"{
            cell.imgTick.isHidden = true
            cell.lblLossFirst.isHidden = false
            cell.winViewFirst.isHidden = true
        }else{
            cell.imgTick.isHidden = false
            cell.lblLossFirst.isHidden = true
            cell.winViewFirst.isHidden = false
        }
        cell.mainView.dropShadow()
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "PickDetail_VC") as! PickDetailVC
        vc.modelObj = modelObj
        vc.strPresent = " Records"
        vc.strIsAllow =  "yes"
        self.navigationController?.pushViewController(vc, animated: true)
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
