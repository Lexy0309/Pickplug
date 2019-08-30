//
//  PickDetailCell.swift
//  PickPlug
//
//  Created by abhinav on 28/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class PickDetailCell: UITableViewCell {

    
    @IBOutlet var lblTitle: AILabel!
    @IBOutlet var lblSubTitle: AILabel!
    @IBOutlet var lblTime: AILabel!
    @IBOutlet var imgBestValue: UIImageView!
    @IBOutlet var viewBuy: UIView!
    @IBOutlet var lblBuy: AILabel!
    @IBOutlet var btnBuy: UIButton!
    @IBOutlet var viewAlreadyBuy: UIView!
    @IBOutlet var lblAlreadyBuy: AILabel!
    @IBOutlet var btnAlreadyBuy: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
