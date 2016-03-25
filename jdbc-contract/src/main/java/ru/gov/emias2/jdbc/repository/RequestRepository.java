package ru.gov.emias2.jdbc.repository;

import ru.gov.emias2.jdbc.*;

import java.util.List;

/**
 * @author mkomlev
 */
public interface RequestRepository {
    <TResult> List<TResult> getList(CollectionRequest<TResult> request);
    <TResult> TResult getOne(ObjectRequest<TResult> request);
    <TPrimitiveType> TPrimitiveType getValue(PrimitiveRequest<TPrimitiveType> request);
    <TIdentityType> TIdentityType insert(InsertRequest<TIdentityType> request);
    int exec(VoidRequest request);
}
