package com.medsync.msuser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class JobDTO implements Serializable {
    private String service;
}
