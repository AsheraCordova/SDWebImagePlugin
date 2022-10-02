package com.ashera.sdwebimage;

import com.ashera.image.ITarget;

/*-[
#import "SDWebImageManager.h"
]-*/
public class SDWebImagePlugin  {
	static class MyBitmap implements r.android.graphics.Bitmap {
    	private Object image;
		public void setImage(Object image) {
			this.image = image;
		}

		@Override
		public int getRowBytes() {
			return getImageWidth(image) * 4;
		}
		
		public static native int getImageWidth(Object value)/*-[
			UIImage* image = ((UIImage*) value);
			return image.size.width;
		]-*/;
		
		public static native int getImageHeight(Object value)/*-[
			UIImage* image = ((UIImage*) value);
			return image.size.height;
		]-*/;

		@Override
		public int getHeight() {
			return getImageHeight(image);
		}

		@Override
		public Object getImage() {
			return image;
		}
	}
    private static final class TargetImageDownloader implements com.ashera.image.IImageDownloader {
        @Override
        public void download(Object context, String url, Object placeholder, Object error, final ITarget target) {
        	target.onPrepareLoad(placeholder);
        	downloadUsingSDWebImageManager(url, error, target, new MyBitmap());
        }
    }
    
	private native static void downloadUsingSDWebImageManager(String imageURL, Object error, final ITarget target, MyBitmap bitmap) /*-[
		SDWebImageManager *manager = [SDWebImageManager sharedManager];
		 [manager loadImageWithURL:[NSURL URLWithString:imageURL]
  			options:0
			progress:^(NSInteger receivedSize, NSInteger expectedSize, NSURL * _Nullable targetURL) {
  			}
			completed:^(UIImage * _Nullable image, NSData * _Nullable data, NSError * _Nullable error, SDImageCacheType cacheType, BOOL finished, NSURL * _Nullable imageURL) {
    			if (error) {
      				[target onBitmapFailedWithId: error];
    			} else if (image) {
      				[bitmap setImageWithId: image];
      				[target onBitmapLoadedWithId: bitmap];
    			}
  			}
  		];
	]-*/;

    public static void initPlugin() {
        // start - body
        // end - body
        com.ashera.image.ImageDownloaderFactory.register(new SDWebImagePlugin.TargetImageDownloader());
    }
}
