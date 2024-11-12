package model;

public record RequestModel(String method, String path, Object request, String authToken){
}
