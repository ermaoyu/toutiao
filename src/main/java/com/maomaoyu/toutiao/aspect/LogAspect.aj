package com.maomaoyu.toutiao.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LogAspect {
    private static  final Logger LOGGER = (Logger) LoggerFactory.getLogger(LogAspect.class);


}
