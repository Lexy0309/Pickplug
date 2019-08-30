//
//  FreePicksVC.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit
import SDWebImage

class FreePicksVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource {
   
    @IBOutlet var headerView: HeaderView!
    @IBOutlet var lblTopHead: AILabel!
    @IBOutlet var dataTableView: UITableView!
    var arrData = NSMutableArray.init()
    var strSpotImage = ""
    var strSpotID = ""
    var strImgUrlPrefix = ""
    var modelDict = NSDictionary.init()
    var arrKeys = NSMutableArray.init()
    var resultObj : NSDictionary!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) + " Picks"
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 86
        dataTableView.sectionHeaderHeight = 50;
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        
        modelDict = (UIApplication.shared.delegate as! AppDelegate).modelObj
        print("modelDict",modelDict)
      //  self.getSportsDetails()
        //strImgUrlPrefix = Utility.getStringSafely(strData: modelDict.object(forKey: "teamImgPrefix"))
        
        if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
            resultObj = modelDict.object(forKey: "allpicks") as! NSDictionary
            let allKeys  = resultObj.allKeys as! [String]
            for i in 0..<allKeys.count {
                if allKeys[i] != "keys" {
                    arrKeys.add(allKeys[i])
                }
            }
        }else{
            resultObj = modelDict.object(forKey: "allpicks") as! NSDictionary
            let allKeys  = resultObj.object(forKey: "keys") as! NSArray
            for i in 0..<allKeys.count {
                let key = (allKeys.object(at: i) as! NSDictionary).object(forKey: "date") as! String
                if resultObj.object(forKey: key) != nil {
                    arrKeys.add(allKeys.object(at: i))
                }
            }
        }
        
        
        
        dataTableView.reloadData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self, selector: #selector(onClickToHome), name: Notification.Name.init(rawValue: "BackToHome"), object: nil)
    }

    @objc func onClickToHome(){
        self.tabBarController?.navigationController?.popViewController(animated: true)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    /*func getSportsDetails(){
        if Utility.isInternetAvailable() {
            Utility.showLoader()
            var strUrl = ""
            if Utility.readString(key: Utility.KEY_IS_TYPE_PICKS) == "Free"{
                strUrl = Utility.GET_FREEPICKS
            }else{
                strUrl = Utility.GET_PICK_BY_ID + Utility.getStringSafely(strData: modelDict.object(forKey: "Id"))
            }
            Utility.sendRequest(forUrl: strUrl, parameters: [:], requestMethod: AIHTTPMethod.get, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        let resultObj = ((response as! NSDictionary).object(forKey: "Results") as! NSDictionary).object(forKey: "allpicks") as! NSArray
        strImgUrlPrefix = Utility.getStringSafely(strData: ((response as! NSDictionary).object(forKey: "Results") as! NSDictionary).object(forKey: "teamImgPrefix"))
        if resultObj.count > 0 {
            arrData = resultObj.mutableCopy() as! NSMutableArray
            dataTableView.reloadData()
        }
        
    }*/
    func onClickBack(sender: Any) {
        self.tabBarController?.navigationController?.popViewController(animated: true)
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle{
        return .lightContent
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        return arrKeys.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
            let key = arrKeys.object(at: section)
            if resultObj.object(forKey: key) != nil {
                return (resultObj.object(forKey: key) as! NSArray).count
            }else{
                return 0;
            }
        }else{
            let key = (arrKeys.object(at: section) as! NSDictionary).object(forKey: "date") as! String
            if resultObj.object(forKey: key) != nil {
                return (resultObj.object(forKey: key) as! NSArray).count
            }else{
                return 0;
            }
        }
        
        
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView(frame: CGRect(x: -10, y: 0, width: tableView.frame.size.width+10, height: 50))
        let label = UILabel(frame: CGRect(x: 0, y: 5, width: tableView.frame.size.width, height: 40))

        if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
            label.text = arrKeys.object(at: section) as? String
        }else{
            label.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: (arrKeys.object(at: section) as! NSDictionary).object(forKey: "date") as? String), withFormat: "EEEE MMM d", enterFormat: "dd-MM-yyyy")
        }
        
        label.textAlignment = .center
        label.textColor = UIColor.init(hex: "6EB15A")
        label.font = UIFont.init(name: "SFUIText-Bold", size: 16)
        view.addSubview(label)
        view.backgroundColor = UIColor.white
        view.dropShadow()
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "FreePicksCell"
        var cell:FreePicksCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? FreePicksCell
        
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? FreePicksCell)!
        }
        
        if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
            let key = arrKeys.object(at: indexPath.section)
            
            let arrPicksData = resultObj.object(forKey: key) as! NSArray
            let modelObj = arrPicksData.object(at: indexPath.row) as! NSDictionary
            //print(modelObj);
            
            cell.imgMiddle.sd_setImage(with: URL(string:Utility.getStringSafely(strData: modelObj.object(forKey: "sportImage"))), completed: nil)
            cell.lblFirst.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgFirst.sd_setImage(with: URL(string: Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
            cell.lblSecond.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgSecond.sd_setImage(with: URL(string: Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }else{
            let arrPicksData = (resultObj.object(forKey: (arrKeys.object(at: indexPath.section) as! NSDictionary).object(forKey: "date") as! String) as! NSArray)
            let modelObj = arrPicksData.object(at: indexPath.row) as! NSDictionary
            
            
            cell.imgMiddle.sd_setImage(with: URL(string:Utility.getStringSafely(strData: modelObj.object(forKey: "sportImage"))), completed: nil)
            cell.lblFirst.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgFirst.sd_setImage(with: URL(string: Utility.getStringSafely(strData: (modelObj.object(forKey: "VisitingTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
            cell.lblSecond.text = Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamName"))
            cell.imgSecond.sd_setImage(with: URL(string: Utility.getStringSafely(strData: (modelObj.object(forKey: "HomeTeamDetails") as! NSDictionary).object(forKey: "TeamIcon"))), completed: nil)
        }
        
        cell.mainView.dropShadow()
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if Utility.readString(key: Utility.KEY_PICKS_SPORT_ID) == "1" {
            let key = arrKeys.object(at: indexPath.section)
            
            let arrPicksData = resultObj.object(forKey: key) as! NSArray
            let modelObj = arrPicksData.object(at: indexPath.row) as! NSDictionary
            
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "PickDetail_VC") as! PickDetailVC
            vc.modelObj = modelObj
            vc.strPresent = " Picks"
            vc.strIsAllow =   Utility.getStringSafely(strData: modelDict.object(forKey: "is_allow"))
            vc.strImgUrlPrefix = strImgUrlPrefix
            vc.strMiddleImage = Utility.getStringSafely(strData: modelDict.object(forKey: "SportIcon"))
            self.navigationController?.pushViewController(vc, animated: true)
            
        }else{
            let arrPicksData = (resultObj.object(forKey: (arrKeys.object(at: indexPath.section) as! NSDictionary).object(forKey: "date") as! String) as! NSArray)
            let modelObj = arrPicksData.object(at: indexPath.row) as! NSDictionary
            
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "PickDetail_VC") as! PickDetailVC
            vc.modelObj = modelObj
            vc.strPresent = " Picks"
            vc.strIsAllow =   Utility.getStringSafely(strData: modelDict.object(forKey: "is_allow"))
            vc.strImgUrlPrefix = strImgUrlPrefix
            vc.strMiddleImage = Utility.getStringSafely(strData: modelDict.object(forKey: "SportIcon"))
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
