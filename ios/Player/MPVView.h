//
//  MPVView.h
//  iPlayClient
//
//  Created by 赫拉 on 2024/3/25.
//

#import <UIKit/UIKit.h>
#import "MPVViewModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface MPVView : UIView
@property (nonatomic, strong) MPVViewModel *viewModel;
@end

NS_ASSUME_NONNULL_END
