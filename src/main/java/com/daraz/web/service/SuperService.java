package com.daraz.web.service;

import java.util.List;

public interface SuperService <Req,Res,ID>{
    Res save(Req dto);
    Res modify(ID id, Req dto);
    boolean remove(ID id);
    Res viewById(ID id);
    List<Res> viewAll();
}