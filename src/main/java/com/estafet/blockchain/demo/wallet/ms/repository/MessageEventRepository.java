package com.estafet.blockchain.demo.wallet.ms.repository;

import com.estafet.blockchain.demo.wallet.ms.model.MessageEvent;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@ViewIndexed(designDoc = "messageEvent")
@N1qlPrimaryIndexed
public interface MessageEventRepository extends CouchbasePagingAndSortingRepository<MessageEvent, String> {
}
