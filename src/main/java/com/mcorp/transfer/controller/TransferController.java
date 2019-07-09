package com.mcorp.transfer.controller;

import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.mcorp.transfer.model.TransferDto;
import com.mcorp.transfer.model.TransferRequest;
import com.mcorp.transfer.model.TransferResponse;
import com.mcorp.transfer.exception.TransferServiceException;
import com.mcorp.transfer.service.TransferService;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

import static spark.Spark.*;

@Singleton
public class TransferController {

    private final TransferService service;
    private final Gson gson;

    @Inject
    public TransferController(TransferService service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    public void initializeRoutes() {
        exception(TransferServiceException.class, this::processException);

        post("/transfers", "application/json", this::processTransfer, gson::toJson);

        get("/transfers/:id", this::processRetrieveTransfer, gson::toJson);

        after((req, resp) -> resp.type("application/json"));
    }

    private void processException(TransferServiceException exception, Request request, Response response) {
        response.status(400);
        response.body(gson.toJson(new TransferResponse(null, exception.getMessage())));
        response.type("application/json");
    }

    private TransferResponse processTransfer(Request request, Response response) {
        String body = request.body().trim();
        TransferRequest transferRequest = gson.fromJson(body, TransferRequest.class);
        Long transferId = service.transfer(transferRequest);
        return new TransferResponse(transferId, "Transfer has been processed successfully");
    }

    private TransferDto processRetrieveTransfer(Request request, Response response) {
        String id = request.params("id");
        return service.retrieveTransferById(Long.valueOf(id));
    }
}
