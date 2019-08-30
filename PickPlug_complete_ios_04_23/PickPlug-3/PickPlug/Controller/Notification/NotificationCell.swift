//
//  NotificationCell.swift
//  PickPlug
//
//  Created by abhinav on 26/06/18.
//  Copyright © 2018 abhinav. All rights reserved.
//

import UIKit

class NotificationCell: UITableViewCell {

    
    @IBOutlet var lblTime: AILabel!
    @IBOutlet var lblData: AILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
