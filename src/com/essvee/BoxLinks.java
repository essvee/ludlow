package com.essvee;

import com.box.sdk.*;
import java.util.HashMap;

class BoxLinks {

    static HashMap<String, String> getURLs() {
        // Connect (temporary token generated at https://nhm.app.box.com/developers/console/app/....../configuration)
        BoxAPIConnection api = new BoxAPIConnection("xxxxxxxxxxxxxxx");

//        BoxFolder boxFolder = new BoxFolder(api, "48732092854"); // test folder
        BoxFolder boxFolder = new BoxFolder(api, "48687719592"); // main LudlowImages folder

        // Create file permissions
        BoxSharedLink.Permissions permissions = new BoxSharedLink.Permissions();
        permissions.setCanDownload(true);
        permissions.setCanPreview(true);

        // Map to store file name and download url
        HashMap<String, String> resultMap = new HashMap<>();

        // Run through every item in the folder adding file name + download URL to map
        Iterable<com.box.sdk.BoxItem.Info> items = boxFolder.getChildren();
        for (BoxItem.Info item : items) {
            if (item instanceof BoxFile.Info) {
                BoxFile.Info fileInto = (BoxFile.Info) item;
                BoxFile file = new BoxFile(api, fileInto.getID());
                BoxSharedLink sharedLink = file.createSharedLink(BoxSharedLink.Access.OPEN, null, permissions);
                String name = fileInto.getName();
                String downloadLink = sharedLink.getDownloadURL();
                resultMap.put(name, downloadLink);
            }
        }
        return resultMap;
    }
}
