//
//  NotificationVC.swift
//  PickPlug
//
//  Created by abhinav on 26/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class NotificationVC: UIViewController,HeaderViewBackDelegate,UITableViewDelegate,UITableViewDataSource,ServiceApiResponseDelegate {

    @IBOutlet var headerView: HeaderView!
    @IBOutlet var dataTableView: UITableView!
    var arrData = NSMutableArray.init()
    var loadMoreView = KRPullLoadView()
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        headerView.backDelegate = self
        headerView.viewMenu.isHidden = true
        headerView.viewBack.isHidden = false
        headerView.lblHeader.text = "Notification"
        dataTableView.delegate = self
        dataTableView.dataSource = self
        dataTableView.estimatedRowHeight = 75
        dataTableView.rowHeight  = UITableViewAutomaticDimension
        dataTableView.tableFooterView = UIView.init()
        
        loadMoreView.delegate = self
        dataTableView.addPullLoadableView(loadMoreView, type: .loadMore)
        loadMoreView.isHidden = true
        
        self.getNotificationDetails(showLoader: true)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func getNotificationDetails(showLoader:Bool){
        if Utility.isInternetAvailable() {
            if (showLoader){
                Utility.showLoader()
            }
            Utility.sendRequest(forUrl: Utility.GET_NOTIFICATION, parameters: ["start_limit":String(arrData.count)], requestMethod: AIHTTPMethod.post, others: [:], withDelegate: self)
        }else{
            Utility.showSnackBar(message: Utility.KEY_NO_INTERNET)
        }
        
    }
    
    func serviceResponseCallBack(response: Any) {
        Utility.hideLoader()
        print("Response ",response)
        if (response as! NSDictionary).object(forKey: "Results") is NSArray {
            let resultObj = (response as! NSDictionary).object(forKey: "Results") as! NSArray
            if resultObj.count > 0 {
                loadMoreView.isHidden = false
                arrData.addObjects(from: resultObj as! [Any])
                self.dataTableView.reloadData()
            }
        }else{
            if arrData.count == 0 {
                Utility.showSnackBar(message: "No record found")
            }else{
                Utility.showSnackBar(message: "No more record found")
            }
        }
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
        return arrData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "NotificationCell"
        var cell:NotificationCell! = dataTableView.dequeueReusableCell(withIdentifier: cellIdentifier) as? NotificationCell
        let modelObj = arrData.object(at: indexPath.row) as! NSDictionary
        if cell == nil{
            let nib:NSArray = Bundle.main.loadNibNamed(cellIdentifier, owner: self, options: nil)! as NSArray
            cell = (nib.object(at: 0) as? NotificationCell)!
        }
        cell.lblTime.text = Utility.formattedDateFromString(dateString: Utility.getStringSafely(strData: modelObj.object(forKey: "created_at")), withFormat: "MMM d, yyyy, h:mm a", enterFormat: "yyyy-dd-MM HH:mm:ss")
        cell.lblData.text = Utility.getStringSafely(strData: modelObj.object(forKey: "message"))
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableViewAutomaticDimension;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
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

extension NotificationVC: KRPullLoadViewDelegate {
    
    func pullLoadView(_ pullLoadView: KRPullLoadView, didChangeState state: KRPullLoaderState, viewType type: KRPullLoaderType) {
        
        if type == .loadMore {
            switch state {
            case let .loading(completionHandler):
                print("Send Load More Request ")
                DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()+0.5) {
                    completionHandler()
                    //self.collectionView.reloadData()
                }
                if self.arrData.count > 0{
                    self.getNotificationDetails(showLoader: false)
                }
            default: break
            }
            return
            
        }
        
        switch state {
            
        case let .loading(completionHandler):
            
            DispatchQueue.main.asyncAfter(deadline: DispatchTime.now()) {
                completionHandler()
            }
        case .none:
            print("none")
        case .pulling( _,_):
            print("pulling")
        }
    }
}

