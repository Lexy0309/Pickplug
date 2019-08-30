//
//  SportsBookCell.swift
//  PickPlug
//
//  Created by abhinav on 27/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class SportsBookCell: UITableViewCell {

    @IBOutlet var imgProfile: UIImageView!
    @IBOutlet var lblTitle: AILabel!
    @IBOutlet var btnJoin: AIButton!
    @IBOutlet var lblDescription: AILabel!
    @IBOutlet var lblOperateSince: AILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        Utility.decorateButton(button: btnJoin, borderColor: "", borderWidth: 0, cornerRadius: btnJoin.frame.size.height / 2)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
