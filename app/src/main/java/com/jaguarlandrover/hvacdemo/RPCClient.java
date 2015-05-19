package com.jaguarlandrover.hvacdemo;
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Jaguar Land Rover.
 *
 * This program is licensed under the terms and conditions of the
 * Mozilla Public License, version 2.0. The full text of the
 * Mozilla Public License is at https://www.mozilla.org/MPL/2.0/
 *
 * File:    RPCClient.java
 * Project: HVACDemo
 *
 * Created by Lilli Szafranski on 5/19/15.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.util.HashMap;

public class RPCClient
{
    private final static String TAG = "HVACDemo:RPCClient";

//    private String mServiceEndpoint;
//
//    public RPCClient(String endpoint) {
//        mServiceEndpoint = endpoint;
//    }
//
//    public void postRequest(RPCRequest request) {
//
//        NSError *jsonError;
//        NSData  *payload = [NSJSONSerialization dataWithJSONObject:[request serialize]
//                                                           options:nil
//                                                             error:&jsonError];
//
//        NSMutableURLRequest *serviceRequest = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:self.serviceEndpoint]];
//
//        [serviceRequest setValue:@"application/json-rpc" forHTTPHeaderField:@"Content-Type"];
//        [serviceRequest setValue:@"objc-JSONRpc/1.0" forHTTPHeaderField:@"User-Agent"];
//
//        [serviceRequest setValue:[NSString stringWithFormat:@"%i", payload.length] forHTTPHeaderField:@"Content-Length"];
//        [serviceRequest setHTTPMethod:@"POST"];
//        [serviceRequest setHTTPBody:payload];
//
//        NSURLResponse *response = nil;
//        NSError       *error    = nil;
//        NSData        *data     = [NSURLConnection sendSynchronousRequest:serviceRequest returningResponse:&response error:&error];
//
//        if(data != nil)
//            handleDataForRequest(data, request);
//        else
//            handleFailedRequestWithError(request, new Error("300"));
//    }
//
//
//    private void handleDataForRequest(NSData data, RPCRequest request) {
//        Error    jsonError = null;
//        id       results   = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers | NSJSONReadingMutableLeaves error:&jsonError];
//
//        if (data.length == 0)
//            request.callback(new RPCResponse(new Error("100")));
//        else if (jsonError)
//            request.callback(new RPCResponse(new Error("200")));
//        else if ([results isKindOfClass:[NSDictionary class]])
//            handleResultForRequest(results, request);
//    }
//
//    private void handleFailedRequestWithError(RPCRequest request, Error error) {
//        request.callback(new RPCResponse(error));
//    }
//
//    private void handleResultForRequest(HashMap result, RPCRequest request) {
//        if (!request.callback)
//            return;
//
//        RPCResponse response = new RPCResponse();
//
//        response.id      = result[@"id"];
//        response.error   = result[@"error"];
//        response.version = result[@"version"];
//        response.result  = result[@"result"];
//
//        request.callback(response);
//    }


}
