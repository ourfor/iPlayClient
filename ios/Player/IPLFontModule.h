//
//  IPLFontModule.h
//  iPlayClient
//
//  Created by 赫拉 on 2024/4/2.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

NS_ASSUME_NONNULL_BEGIN

@interface IPLFontModule : NSObject<RCTBridgeModule>
+ (NSArray<NSString *> *)installedFontName;
+ (NSArray<NSString *> *)installedFontFamilyName;
@end

NS_ASSUME_NONNULL_END
