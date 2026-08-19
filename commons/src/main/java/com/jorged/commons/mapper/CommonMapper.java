package com.jorged.commons.mapper;

public interface CommonMapper<RQ, RS, E> {
    E requestAEntidad(RQ request);
    RS entidadResponse(E entidad);

}
