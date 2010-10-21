package com.springsource.greenhouse.connect;

import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

@Component
public class TwitterConnectInterceptor implements ConnectInterceptor<TwitterOperations> {

	private static final String POST_TWEET_PARAM = "postTweet";

	public boolean supportsProvider(String providerName) {
		return providerName.equals("twitter");
	}

	public void preConnect(AccountProvider<TwitterOperations> provider, WebRequest request) {
		String tweetIt = request.getParameter(POST_TWEET_PARAM);
		if (StringUtils.hasText(tweetIt)) {
			request.setAttribute(POST_TWEET_PARAM, true, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(AccountProvider<TwitterOperations> provider, Account account, WebRequest request) {
		if (request.getAttribute(POST_TWEET_PARAM, WebRequest.SCOPE_SESSION) != null) {
			try {
				provider.getApi(account.getId()).setStatus("Join me at the Greenhouse! " + account.getProfileUrl());
			} catch (DuplicateTweetException doesntMatter) {
			}
			request.removeAttribute(POST_TWEET_PARAM, WebRequest.SCOPE_SESSION);
		}
	}
}