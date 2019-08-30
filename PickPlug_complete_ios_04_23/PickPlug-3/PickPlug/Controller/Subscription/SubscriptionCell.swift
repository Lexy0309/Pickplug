//
//  SubscriptionCell.swift
//  PickPlug
//
//  Created by abhinav on 29/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class SubscriptionCell: UITableViewCell {

    @IBOutlet var lblTitle: AILabel!
    @IBOutlet var viewBestValue: UIView!
    
    @IBOutlet weak var btnSubsDetail: UIButton!
    @IBOutlet weak var lblEndDateHeightCons: NSLayoutConstraint!
    @IBOutlet weak var lblEndDate: UILabel!
    @IBOutlet var viewBuy: UIView!
    @IBOutlet var lblBuy: AILabel!
    @IBOutlet var btnBuy: UIButton!
    @IBOutlet var viewAlreadyBuy: UIView!
    @IBOutlet var lblAlreadyBuy: AILabel!
    @IBOutlet var btnAlreadyBuy: UIButton!
    @IBOutlet weak var subsDetailHeight: NSLayoutConstraint!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        Utility.decorateView(view: viewBuy, borderColor: "", borderWidth: 0, cornerRadius: viewBuy.frame.size.height / 2)
        Utility.decorateView(view: viewAlreadyBuy, borderColor: "", borderWidth: 0, cornerRadius: viewAlreadyBuy.frame.size.height / 2)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
