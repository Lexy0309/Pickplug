
//
//  AIWebService.swift
//  Yamiraan Azadari
//
//  Created by Archive Infotech on 09/11/17.
//  Copyright © 2017 Archive Infotech. All rights reserved.
//

import Foundation
import UIKit
import Alamofire


protocol ServiceApiResponseDelegate {
    func serviceResponseCallBack(response:Any)
}

class AIWebService {
    
    var mDelegate : ServiceApiResponseDelegate!
    var request : DataRequest!
    
    func cancelReq(){
        Alamofire.SessionManager.default.session.getAllTasks { (tasks) in
            print("Cancel Task")
            tasks.forEach({$0.cancel()})
        }
    }
    
    func sendRequest(forUrl: String,parameters:[String: String],requestMethod : AIHTTPMethod,others:[String: AnyObject],withDelegate:ServiceApiResponseDelegate){
        
        mDelegate = withDelegate
        if Utility.isInternetAvailable() {
            var reqMethod  = HTTPMethod.get
            if requestMethod == .post {
                reqMethod  = HTTPMethod.post
            }
            
            let manager = Alamofire.SessionManager.default
            manager.session.configuration.timeoutIntervalForRequest = 60
            request = manager.request(forUrl, method: reqMethod, parameters: parameters, encoding:URLEncoding.httpBody).responseJSON { response in
                
                 switch response.result {
                 
                 case .success(_):
                    if let json = response.result.value {
                        //print("For URl :  ",forUrl,"\n, Request : ", parameters,"\n JSON: \(json)")
                        self.mDelegate.serviceResponseCallBack(response: json)
                    }
                 case .failure(let error):
                    Utility.hideLoader();
                    print("API Error",error.localizedDescription);
                    Utility.showSnackBar(message: "API Error Timeout "+error.localizedDescription);
                }
            }
        }
    }
    
    func sendRequestMultipart(forUrl: String,parameters:[String: String],requestMethod : AIHTTPMethod,imgParameter: String,imgFileName: String,imageFile : UIImage,withDelegate:ServiceApiResponseDelegate){
       
        mDelegate = withDelegate
        if Utility.isInternetAvailable() {
            var reqMethod  = HTTPMethod.get
            if requestMethod == .post {
                reqMethod  = HTTPMethod.post
            }
            
            let manager = Alamofire.SessionManager.default
            manager.session.configuration.timeoutIntervalForRequest = 60
            
        
            manager.upload(multipartFormData: { (multipartData) in
                if let imageData = UIImageJPEGRepresentation(imageFile, 0.5) {
                    multipartData.append(imageData, withName: imgParameter, fileName: imgFileName, mimeType: "image/jpeg")
                }
                
                for (key, value) in parameters {
                    multipartData.append(value.data(using: String.Encoding.utf8)!, withName: key)
                    
                }
            }, to: forUrl,method: reqMethod, headers: [:], encodingCompletion: { (result) in
                switch result {
                case .success(let upload):
                    upload.request.responseJSON(completionHandler: { (response) in
                        if let json = response.result.value{
                            self.mDelegate.serviceResponseCallBack(response: json)
                        }
                    })
                    
                case .failure(let error):
                    Utility.hideLoader()
                    print("API Error",error.localizedDescription);
                    Utility.showSnackBar(message: "API Error Timeout "+error.localizedDescription);
                }
            })
        }
    }
}
