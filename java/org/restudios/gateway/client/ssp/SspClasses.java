package org.restudios.gateway.client.ssp;

import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
public class SspClasses {
    public static class TransferredFile {
        public long length;
        public byte[] data;

        public TransferredFile(long length, byte[] data) {
            this.length = length;
            this.data = data;
        }
    }

    public static class EventClasses {

        public static class UserAckMessage extends SspClasses.GeneralClasses.SlClass {
            public Long chatId;
            @Nullable
            public Long serverId;
            public Long messageId;

            public UserAckMessage(Long chatId, @Nullable Long serverId, Long messageId) {
                super();
                this.chatId = chatId;
                this.serverId = serverId;
                this.messageId = messageId;
            }
        }

        public static class GroupMetaUpdated extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.Group group;

            public GroupMetaUpdated(SspClasses.GeneralClasses.Group group) {
                super();
                this.group = group;
            }
        }

        public static class ChatRemove extends SspClasses.GeneralClasses.SlClass {
            public Long chatId;

            public ChatRemove(Long chatId) {
                super();
                this.chatId = chatId;
            }
        }

        public static class ChatCreated extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.Chat chat;

            public ChatCreated(SspClasses.GeneralClasses.Chat chat) {
                super();
                this.chat = chat;
            }
        }

        public static class MessageUnpinned extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.Message message;

            public MessageUnpinned(SspClasses.GeneralClasses.Message message) {
                super();
                this.message = message;
            }
        }

        public static class MessagePinned extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.Message message;

            public MessagePinned(SspClasses.GeneralClasses.Message message) {
                super();
                this.message = message;
            }
        }

        public static class UserPostMessage extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.Message message;

            public UserPostMessage(SspClasses.GeneralClasses.Message message) {
                super();
                this.message = message;
            }
        }
    }

    public static class ExtraClasses {

        public static class GroupPrivateAccessData extends SspClasses.ExtraClasses.GroupAccessData {
        }

        public static class GroupPublicAccessData extends SspClasses.ExtraClasses.GroupAccessData {
            public String tag;

            public GroupPublicAccessData(String tag) {
                super();
                this.tag = tag;
            }
        }

        public static abstract class GroupAccessData extends SspClasses.GeneralClasses.SlClass {
        }

        public static class AwaitingForFiles extends SspClasses.GeneralClasses.SlClass {
            public List<String> keys;

            public AwaitingForFiles(List<String> keys) {
                super();
                this.keys = keys;
            }
        }

        public static class UploadFileCompleteResult extends SspClasses.GeneralClasses.SlClass {
            public String id;

            public UploadFileCompleteResult(String id) {
                super();
                this.id = id;
            }
        }

        public static class UploadFileResult extends SspClasses.GeneralClasses.SlClass {
            public String id;
            public Long timeout;

            public UploadFileResult(String id, Long timeout) {
                super();
                this.id = id;
                this.timeout = timeout;
            }
        }

        public enum Currency {
            USD,
            EUR,
            JPY,
            CNY,
            GBP,
            AUD,
            CAD,
            CHF,
            NZD,
            RUB,
            SGD,
            MXN,
            TRY,
            UAH,
            SEK,
            NOK,
            DKK,
            PLN,
            HUF,
            CZK,
            RON,
            BGN,
            ISK,
            SPK,
        }

        public static class SessionList extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.SettingsClasses.Sessions.Session current;
            public List<SspClasses.SettingsClasses.Sessions.Session> sessions;

            public SessionList(SspClasses.SettingsClasses.Sessions.Session current, List<SspClasses.SettingsClasses.Sessions.Session> sessions) {
                super();
                this.current = current;
                this.sessions = sessions;
            }
        }

        public static class TagAvailability extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.ExtraClasses.TagAvailabilityType availability;

            public TagAvailability(SspClasses.ExtraClasses.TagAvailabilityType availability) {
                super();
                this.availability = availability;
            }
        }

        public enum TagAvailabilityType {
            AVAILABLE,
            EXISTS,
            BANNED,
        }

        public static class Pong extends SspClasses.GeneralClasses.SlClass {
        }

        public static class ChatAckState extends SspClasses.ExtraClasses.AckState {
            public Long chatId;
            @Nullable
            public Long serverId;

            public ChatAckState(Long chatId, @Nullable Long serverId, Long recipientLastMessageReadId, int unreadMessages) {
                super(recipientLastMessageReadId, unreadMessages);
                this.chatId = chatId;
                this.serverId = serverId;
            }
        }

        public static class AckState extends SspClasses.GeneralClasses.SlClass {
            public Long recipientLastMessageReadId;
            public int unreadMessages;

            public AckState(Long recipientLastMessageReadId, int unreadMessages) {
                super();
                this.recipientLastMessageReadId = recipientLastMessageReadId;
                this.unreadMessages = unreadMessages;
            }
        }

        public static class MessageId extends SspClasses.GeneralClasses.SlClass {
            public Long messageId;

            public MessageId(Long messageId) {
                super();
                this.messageId = messageId;
            }
        }

        public static class ListQuery extends SspClasses.GeneralClasses.SlClass {
            @Nullable
            public String pointer;
            public SspClasses.ExtraClasses.ListDirection direction;

            public ListQuery(@Nullable String pointer, SspClasses.ExtraClasses.ListDirection direction) {
                super();
                this.pointer = pointer;
                this.direction = direction;
            }
        }

        public enum ListDirection {
            latest,
            next,
            prev,
        }

        public static class GroupMemberList extends SspClasses.ExtraClasses.ListResponse<SspClasses.GeneralClasses.GroupMember> {
            public GroupMemberList(List<SspClasses.GeneralClasses.GroupMember> items, boolean hasNext, boolean hasPrev, @Nullable String nextCursor, @Nullable String prevCursor) {
                super(items, hasNext, hasPrev, nextCursor, prevCursor);
            }
        }

        public static class ChatMessageList extends SspClasses.ExtraClasses.ListResponse<SspClasses.GeneralClasses.Message> {
            public ChatMessageList(List<SspClasses.GeneralClasses.Message> items, boolean hasNext, boolean hasPrev, @Nullable String nextCursor, @Nullable String prevCursor) {
                super(items, hasNext, hasPrev, nextCursor, prevCursor);
            }
        }

        public static class ListResponse<T> extends SspClasses.GeneralClasses.SlClass {
            public List<T> items;
            public boolean hasNext;
            public boolean hasPrev;
            @Nullable
            public String nextCursor;
            @Nullable
            public String prevCursor;

            public ListResponse(List<T> items, boolean hasNext, boolean hasPrev, @Nullable String nextCursor, @Nullable String prevCursor) {
                super();
                this.items = items;
                this.hasNext = hasNext;
                this.hasPrev = hasPrev;
                this.nextCursor = nextCursor;
                this.prevCursor = prevCursor;
            }
        }

        public static class PeerList extends SspClasses.GeneralClasses.SlClass {
            public List<SspClasses.GeneralClasses.Peer> peers;
            public int total;
            public int left;

            public PeerList(List<SspClasses.GeneralClasses.Peer> peers, int total, int left) {
                super();
                this.peers = peers;
                this.total = total;
                this.left = left;
            }
        }

        public static class ChatList extends SspClasses.GeneralClasses.SlClass {
            public List<SspClasses.GeneralClasses.Chat> chats;
            public int total;
            public int left;

            public ChatList(List<SspClasses.GeneralClasses.Chat> chats, int total, int left) {
                super();
                this.chats = chats;
                this.total = total;
                this.left = left;
            }
        }

        public static class FriendRequestList extends SspClasses.GeneralClasses.SlClass {
            public List<SspClasses.ExtraClasses.FriendRequest> requests;

            public FriendRequestList(List<SspClasses.ExtraClasses.FriendRequest> requests) {
                super();
                this.requests = requests;
            }
        }

        public static class ChangeMetaSuccessfully extends SspClasses.GeneralClasses.SlClass {
        }

        public static class UserBannerImageInput extends SspClasses.ExtraClasses.UserBannerInput {
            @Nullable
            public SspClasses.TransferredFile file;
            public SspClasses.GeneralClasses.LinearGradientOpacity gradient;

            public UserBannerImageInput(@Nullable SspClasses.TransferredFile file, SspClasses.GeneralClasses.LinearGradientOpacity gradient) {
                super();
                this.file = file;
                this.gradient = gradient;
            }
        }

        public static class UserBannerGradientInput extends SspClasses.ExtraClasses.UserBannerInput {
            public SspClasses.GeneralClasses.LinearGradient gradient;

            public UserBannerGradientInput(SspClasses.GeneralClasses.LinearGradient gradient) {
                super();
                this.gradient = gradient;
            }
        }

        public static class UserBannerInput extends SspClasses.GeneralClasses.SlClass {
        }

        public static class FriendRequest extends SspClasses.GeneralClasses.IncludableClass<SspClasses.ExtraClasses.FriendRequest> {
        }
    }

    public static class FunctionClasses {

        public static class Ping extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.Pong> {
        }

        public static class FileTransferFunctions {

            public static class UploadFileComplete extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.UploadFileCompleteResult> {
                public String id;
                public String md5;

                public UploadFileComplete(String id, String md5) {
                    super();
                    this.id = id;
                    this.md5 = md5;
                }
            }

            public static class UploadFileChunk extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public String id;
                public long offset;
                public byte[] data;

                public UploadFileChunk(String id, long offset, byte[] data) {
                    super();
                    this.id = id;
                    this.offset = offset;
                    this.data = data;
                }
            }

            public static class UploadFile extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.UploadFileResult> {
                public String filename;
                public Long length;
                public int chunks;

                public UploadFile(String filename, Long length, int chunks) {
                    super();
                    this.filename = filename;
                    this.length = length;
                    this.chunks = chunks;
                }
            }
        }

        public static class GetFileLength extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
            public SspClasses.TransferredFile file;

            public GetFileLength(SspClasses.TransferredFile file) {
                super();
                this.file = file;
            }
        }

        public static class AuthorizationFunctions {

            public static class IsTagAvailable extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.TagAvailability> {
                public String tag;

                public IsTagAvailable(String tag) {
                    super();
                    this.tag = tag;
                }
            }
        }

        public static class ChatFunctions {

            public static class GetPeerByTag extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Peer> {
                public String tag;

                public GetPeerByTag(String tag) {
                    super();
                    this.tag = tag;
                }
            }

            public static class GetUserByTag extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.User> {
                public String tag;

                public GetUserByTag(String tag) {
                    super();
                    this.tag = tag;
                }
            }

            public static class SetMessageAck extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                @Nullable
                public Long serverId;
                public Long messageId;

                public SetMessageAck(Long chatId, @Nullable Long serverId, Long messageId) {
                    super();
                    this.chatId = chatId;
                    this.serverId = serverId;
                    this.messageId = messageId;
                }
            }

            public static class UnpinMessage extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public Long messageId;

                public UnpinMessage(Long chatId, Long messageId) {
                    super();
                    this.chatId = chatId;
                    this.messageId = messageId;
                }
            }

            public static class PinMessage extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public Long messageId;

                public PinMessage(Long chatId, Long messageId) {
                    super();
                    this.chatId = chatId;
                    this.messageId = messageId;
                }
            }

            public static class ClearChatHistory extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public Long messageId;

                public ClearChatHistory(Long chatId, Long messageId) {
                    super();
                    this.chatId = chatId;
                    this.messageId = messageId;
                }
            }

            public static class DeleteMessages extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public List<Long> messageIds;

                public DeleteMessages(Long chatId, List<Long> messageIds) {
                    super();
                    this.chatId = chatId;
                    this.messageIds = messageIds;
                }
            }

            public static class DeleteMessage extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public Long messageId;

                public DeleteMessage(Long chatId, Long messageId) {
                    super();
                    this.chatId = chatId;
                    this.messageId = messageId;
                }
            }

            public static class GetChatAckState extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.AckState> {
                public Long chatId;
                @Nullable
                public Long serverId;

                public GetChatAckState(Long chatId, @Nullable Long serverId) {
                    super();
                    this.chatId = chatId;
                    this.serverId = serverId;
                }
            }

            public static class GetPinnedChatMessages extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChatMessageList> {
                public Long chatId;
                public SspClasses.ExtraClasses.ListQuery query;

                public GetPinnedChatMessages(Long chatId, SspClasses.ExtraClasses.ListQuery query) {
                    super();
                    this.chatId = chatId;
                    this.query = query;
                }
            }

            public static class LeaveGroup extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long groupId;

                public LeaveGroup(Long groupId) {
                    super();
                    this.groupId = groupId;
                }
            }

            public static class KickGroupMember extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long groupId;
                public SspClasses.GeneralClasses.UserInput input;

                public KickGroupMember(Long groupId, SspClasses.GeneralClasses.UserInput input) {
                    super();
                    this.groupId = groupId;
                    this.input = input;
                }
            }

            public static class GetGroupMember extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.GroupMember> {
                public Long groupId;
                public SspClasses.GeneralClasses.UserInput input;

                public GetGroupMember(Long groupId, SspClasses.GeneralClasses.UserInput input) {
                    super();
                    this.groupId = groupId;
                    this.input = input;
                }
            }

            public static class GetGroupMembers extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.GroupMemberList> {
                public Long groupId;
                public SspClasses.ExtraClasses.ListQuery query;

                public GetGroupMembers(Long groupId, SspClasses.ExtraClasses.ListQuery query) {
                    super();
                    this.groupId = groupId;
                    this.query = query;
                }
            }

            public static class GetChatMessages extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChatMessageList> {
                public Long chatId;
                public SspClasses.ExtraClasses.ListQuery query;

                public GetChatMessages(Long chatId, SspClasses.ExtraClasses.ListQuery query) {
                    super();
                    this.chatId = chatId;
                    this.query = query;
                }
            }

            public static class GetPeers extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.PeerList> {
                public int limit;
                public int offset;
                public boolean users;
                public boolean groups;

                public GetPeers(int limit, int offset, boolean users, boolean groups) {
                    super();
                    this.limit = limit;
                    this.offset = offset;
                    this.users = users;
                    this.groups = groups;
                }
            }

            public static class GetChats extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChatList> {
                public int limit;
                public int offset;
                public boolean users;
                public boolean groups;

                public GetChats(int limit, int offset, boolean users, boolean groups) {
                    super();
                    this.limit = limit;
                    this.offset = offset;
                    this.users = users;
                    this.groups = groups;
                }
            }

            public static class CreateGroupChat extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Chat> {
                public List<SspClasses.GeneralClasses.InputUserId> users;
                public String name;
                public SspClasses.ExtraClasses.GroupAccessData access;
                @Nullable
                public String description;
                @Nullable
                public SspClasses.TransferredFile avatar;

                public CreateGroupChat(List<SspClasses.GeneralClasses.InputUserId> users, String name, SspClasses.ExtraClasses.GroupAccessData access, @Nullable String description, @Nullable SspClasses.TransferredFile avatar) {
                    super();
                    this.users = users;
                    this.name = name;
                    this.access = access;
                    this.description = description;
                    this.avatar = avatar;
                }
            }

            public static class JoinGroupChat extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Chat> {
                public String tag;

                public JoinGroupChat(String tag) {
                    super();
                    this.tag = tag;
                }
            }

            public static class GetUser extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.User> {
                public Long userId;
                public Long accessHash;

                public GetUser(Long userId, Long accessHash) {
                    super();
                    this.userId = userId;
                    this.accessHash = accessHash;
                }
            }

            public static class CreateUserChat extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Chat> {
                public Long userId;
                public Long accessHash;

                public CreateUserChat(Long userId, Long accessHash) {
                    super();
                    this.userId = userId;
                    this.accessHash = accessHash;
                }
            }

            public static class SetGroupTitle extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public Long chatId;
                public String title;

                public SetGroupTitle(Long chatId, String title) {
                    super();
                    this.chatId = chatId;
                    this.title = title;
                }
            }

            public static class GetChatCallInfo extends SspClasses.FunctionClasses.FunctionClass<SspClasses.VoiceClasses.CallInfo> {
                public Long chatId;

                public GetChatCallInfo(Long chatId) {
                    super();
                    this.chatId = chatId;
                }
            }

            public static class Call extends SspClasses.FunctionClasses.FunctionClass<SspClasses.VoiceClasses.VoiceChannelData> {
                public Long chatId;

                public Call(Long chatId) {
                    super();
                    this.chatId = chatId;
                }
            }

            public static class GetChat extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Chat> {
                public Long chatId;
                @Nullable
                public Long serverId;

                public GetChat(Long chatId, @Nullable Long serverId) {
                    super();
                    this.chatId = chatId;
                    this.serverId = serverId;
                }
            }
        }

        public static class MessageFunctions {

            public static class SendVideoNote extends SspClasses.FunctionClasses.MessageFunctions.SendMessage<SspClasses.ExtraClasses.AwaitingForFiles> {
                public SspClasses.GeneralClasses.VideoNoteUpload videoNote;

                public SendVideoNote(SspClasses.GeneralClasses.VideoNoteUpload videoNote, Long chatId, @Nullable Long serverId, Long nonce, Long replyMessageId, String content) {
                    super(chatId, serverId, nonce, replyMessageId, content);
                    this.videoNote = videoNote;
                }
            }

            public static class SendVoiceMessage extends SspClasses.FunctionClasses.MessageFunctions.SendMessage<SspClasses.ExtraClasses.AwaitingForFiles> {
                public SspClasses.GeneralClasses.VoiceMessageUpload voice;

                public SendVoiceMessage(SspClasses.GeneralClasses.VoiceMessageUpload voice, Long chatId, @Nullable Long serverId, Long nonce, Long replyMessageId, String content) {
                    super(chatId, serverId, nonce, replyMessageId, content);
                    this.voice = voice;
                }
            }

            public static class SendFileMessage extends SspClasses.FunctionClasses.MessageFunctions.SendMessage<SspClasses.ExtraClasses.AwaitingForFiles> {
                public List<SspClasses.GeneralClasses.MessageFileUpload> files;

                public SendFileMessage(List<SspClasses.GeneralClasses.MessageFileUpload> files, Long chatId, @Nullable Long serverId, Long nonce, Long replyMessageId, String content) {
                    super(chatId, serverId, nonce, replyMessageId, content);
                    this.files = files;
                }
            }

            public static class SendTextMessage extends SspClasses.FunctionClasses.MessageFunctions.SendMessage<SspClasses.GeneralClasses.Nope> {
                public SendTextMessage(Long chatId, @Nullable Long serverId, Long nonce, Long replyMessageId, String content) {
                    super(chatId, serverId, nonce, replyMessageId, content);
                }
            }

            public static abstract class SendMessage<T extends SspClasses.GeneralClasses.SlClass> extends SspClasses.FunctionClasses.FunctionClass<T> {
                public Long chatId;
                @Nullable
                public Long serverId;
                public Long nonce;
                public Long replyMessageId;
                public String content;

                public SendMessage(Long chatId, @Nullable Long serverId, Long nonce, Long replyMessageId, String content) {
                    super();
                    this.chatId = chatId;
                    this.serverId = serverId;
                    this.nonce = nonce;
                    this.replyMessageId = replyMessageId;
                    this.content = content;
                }
            }
        }

        public static class UserFunctions {

            public static class SetListenGroupIds extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public List<Long> groups;

                public SetListenGroupIds(List<Long> groups) {
                    super();
                    this.groups = groups;
                }
            }

            public static class SetListenUserIds extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public List<SspClasses.GeneralClasses.UserInput> users;

                public SetListenUserIds(List<SspClasses.GeneralClasses.UserInput> users) {
                    super();
                    this.users = users;
                }
            }

            public static class ChangeAvatar extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChangeMetaSuccessfully> {
                public SspClasses.TransferredFile file;

                public ChangeAvatar(SspClasses.TransferredFile file) {
                    super();
                    this.file = file;
                }
            }

            public static class ChangeBanner extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChangeMetaSuccessfully> {
                public SspClasses.ExtraClasses.UserBannerInput banner;

                public ChangeBanner(SspClasses.ExtraClasses.UserBannerInput banner) {
                    super();
                    this.banner = banner;
                }
            }

            public static class ChangeBio extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChangeMetaSuccessfully> {
                public String bio;

                public ChangeBio(String bio) {
                    super();
                    this.bio = bio;
                }
            }

            public static class ChangeDisplayName extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.ChangeMetaSuccessfully> {
                public String displayName;

                public ChangeDisplayName(String displayName) {
                    super();
                    this.displayName = displayName;
                }
            }

            public static class SetOnlineStatus extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public SspClasses.GeneralClasses.OnlineStatusType onlineStatus;

                public SetOnlineStatus(SspClasses.GeneralClasses.OnlineStatusType onlineStatus) {
                    super();
                    this.onlineStatus = onlineStatus;
                }
            }

            public static class ChangeTag extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
                public String tag;

                public ChangeTag(String tag) {
                    super();
                    this.tag = tag;
                }
            }

            public static class GetFriendRequests extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.FriendRequestList> {
            }
        }

        public static class SettingsFunctions {

            public static class DropAllSessions extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.Nope> {
            }

            public static class DropSession extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.SessionList> {
                public Long id;

                public DropSession(Long id) {
                    super();
                    this.id = id;
                }
            }

            public static class GetActiveSessions extends SspClasses.FunctionClasses.FunctionClass<SspClasses.ExtraClasses.SessionList> {
            }
        }

        public static class GetMe extends SspClasses.FunctionClasses.FunctionClass<SspClasses.GeneralClasses.User> {
        }

        public static abstract class FunctionClass<T extends SspClasses.GeneralClasses.SlClass> extends SspClasses.GeneralClasses.SlClass {
        }

        public static abstract class AvailableDuringAuthorization {
        }
    }

    public static class GeneralClasses {

        public static class UnnamedFileUpload extends SspClasses.GeneralClasses.SlClass {
            public int chunks;
            public Long length;

            public UnnamedFileUpload(int chunks, Long length) {
                super();
                this.chunks = chunks;
                this.length = length;
            }
        }

        public static class VideoNoteUpload extends SspClasses.GeneralClasses.SlClass {
            public String nonce;
            public Long length;
            public int chunks;
            public int duration;
            public int size;

            public VideoNoteUpload(String nonce, Long length, int chunks, int duration, int size) {
                super();
                this.nonce = nonce;
                this.length = length;
                this.chunks = chunks;
                this.duration = duration;
                this.size = size;
            }
        }

        public static class VoiceMessageUpload extends SspClasses.GeneralClasses.SlClass {
            public String nonce;
            public Long length;
            public int chunks;

            public VoiceMessageUpload(String nonce, Long length, int chunks) {
                super();
                this.nonce = nonce;
                this.length = length;
                this.chunks = chunks;
            }
        }

        public static class MessageFileUpload extends SspClasses.GeneralClasses.SlClass {
            public String nonce;
            public Long length;
            public int chunks;
            public String name;

            public MessageFileUpload(String nonce, Long length, int chunks, String name) {
                super();
                this.nonce = nonce;
                this.length = length;
                this.chunks = chunks;
                this.name = name;
            }
        }

        public static class MessageFile extends SspClasses.GeneralClasses.SlClass {
            public String filename;
            public Long length;
            public SspClasses.GeneralClasses.CdnFile file;

            public MessageFile(String filename, Long length, SspClasses.GeneralClasses.CdnFile file) {
                super();
                this.filename = filename;
                this.length = length;
                this.file = file;
            }
        }

        public static class VideoNoteMessage extends SspClasses.GeneralClasses.Message {
            public SspClasses.GeneralClasses.CdnFile file;
            public SspClasses.GeneralClasses.CdnFile thumbnail;
            public Long length;
            public int duration;
            public int size;

            public VideoNoteMessage(SspClasses.GeneralClasses.CdnFile file, SspClasses.GeneralClasses.CdnFile thumbnail, Long length, int duration, int size, Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super(chatId, serverId, author, messageId, nonce, content, reply, timestamp);
                this.file = file;
                this.thumbnail = thumbnail;
                this.length = length;
                this.duration = duration;
                this.size = size;
            }
        }

        public static class VoiceMessage extends SspClasses.GeneralClasses.Message {
            public SspClasses.GeneralClasses.CdnFile file;
            public Long length;
            public int duration;
            public List<Float> visualization;

            public VoiceMessage(SspClasses.GeneralClasses.CdnFile file, Long length, int duration, List<Float> visualization, Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super(chatId, serverId, author, messageId, nonce, content, reply, timestamp);
                this.file = file;
                this.length = length;
                this.duration = duration;
                this.visualization = visualization;
            }
        }

        public static class FileMessage extends SspClasses.GeneralClasses.Message {
            public List<SspClasses.GeneralClasses.MessageFile> files;

            public FileMessage(List<SspClasses.GeneralClasses.MessageFile> files, Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super(chatId, serverId, author, messageId, nonce, content, reply, timestamp);
                this.files = files;
            }
        }

        public static class GroupJoinMessage extends SspClasses.GeneralClasses.Message {
            public GroupJoinMessage(Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super(chatId, serverId, author, messageId, nonce, content, reply, timestamp);
            }
        }

        public static class TextMessage extends SspClasses.GeneralClasses.Message {
            public TextMessage(Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super(chatId, serverId, author, messageId, nonce, content, reply, timestamp);
            }
        }

        public static abstract class Message extends SspClasses.GeneralClasses.IncludableClass<SspClasses.GeneralClasses.Message> {
            public Long chatId;
            @Nullable
            public Long serverId;
            public SspClasses.GeneralClasses.MessageAuthor author;
            public Long messageId;
            public Long nonce;
            public String content;
            public Long reply;
            public Instant timestamp;

            public Message(Long chatId, @Nullable Long serverId, SspClasses.GeneralClasses.MessageAuthor author, Long messageId, Long nonce, String content, Long reply, Instant timestamp) {
                super();
                this.chatId = chatId;
                this.serverId = serverId;
                this.author = author;
                this.messageId = messageId;
                this.nonce = nonce;
                this.content = content;
                this.reply = reply;
                this.timestamp = timestamp;
            }
        }

        public static class SystemMessageAuthor extends SspClasses.GeneralClasses.MessageAuthor {
        }

        public static class UserMessageAuthor extends SspClasses.GeneralClasses.MessageAuthor {
            public SspClasses.GeneralClasses.User user;

            public UserMessageAuthor(SspClasses.GeneralClasses.User user) {
                super();
                this.user = user;
            }
        }

        public static class MessageAuthor extends SspClasses.GeneralClasses.SlClass {
        }

        public enum AckState {
            sent,
            seen,
            unk,
        }

        public static class ChatMessage extends SspClasses.GeneralClasses.SlClass {
            @Nullable
            public SspClasses.GeneralClasses.Message message;

            public ChatMessage(@Nullable SspClasses.GeneralClasses.Message message) {
                super();
                this.message = message;
            }
        }

        public static class GroupChat extends SspClasses.GeneralClasses.Chat {
            public SspClasses.GeneralClasses.Group group;

            public GroupChat(SspClasses.GeneralClasses.Group group, Long id, SspClasses.GeneralClasses.ChatType chatType, SspClasses.GeneralClasses.ChatMessage lastMessage, String title, @Nullable Integer pinOrder, @Nullable SspClasses.VoiceClasses.CallInfo call, Instant timestamp, boolean archived, boolean pinned, int pinnedPriority) {
                super(id, chatType, lastMessage, title, pinOrder, call, timestamp, archived, pinned, pinnedPriority);
                this.group = group;
            }
        }

        public static class DirectChat extends SspClasses.GeneralClasses.Chat {
            public SspClasses.GeneralClasses.User directRecipient;
            public SspClasses.ExtraClasses.AckState ackState;

            public DirectChat(SspClasses.GeneralClasses.User directRecipient, SspClasses.ExtraClasses.AckState ackState, Long id, SspClasses.GeneralClasses.ChatType chatType, SspClasses.GeneralClasses.ChatMessage lastMessage, String title, @Nullable Integer pinOrder, @Nullable SspClasses.VoiceClasses.CallInfo call, Instant timestamp, boolean archived, boolean pinned, int pinnedPriority) {
                super(id, chatType, lastMessage, title, pinOrder, call, timestamp, archived, pinned, pinnedPriority);
                this.directRecipient = directRecipient;
                this.ackState = ackState;
            }
        }

        public static class ChatLocation extends SspClasses.GeneralClasses.SlClass {
            public Long chatId;
            @Nullable
            public Long serverId;

            public ChatLocation(Long chatId, @Nullable Long serverId) {
                super();
                this.chatId = chatId;
                this.serverId = serverId;
            }
        }

        public static class GroupPeer extends SspClasses.GeneralClasses.Peer {
            public SspClasses.GeneralClasses.Group group;

            public GroupPeer(SspClasses.GeneralClasses.Group group) {
                super();
                this.group = group;
            }
        }

        public static class UserPeer extends SspClasses.GeneralClasses.Peer {
            public SspClasses.GeneralClasses.User user;

            public UserPeer(SspClasses.GeneralClasses.User user) {
                super();
                this.user = user;
            }
        }

        public static abstract class Peer extends SspClasses.GeneralClasses.SlClass {
        }

        public static abstract class Chat extends SspClasses.GeneralClasses.IncludableClass<SspClasses.GeneralClasses.Chat> {
            public Long id;
            public SspClasses.GeneralClasses.ChatType chatType;
            public SspClasses.GeneralClasses.ChatMessage lastMessage;
            public String title;
            @Nullable
            public Integer pinOrder;
            @Nullable
            public SspClasses.VoiceClasses.CallInfo call;
            public Instant timestamp;
            public boolean archived;
            public boolean pinned;
            public int pinnedPriority;

            public Chat(Long id, SspClasses.GeneralClasses.ChatType chatType, SspClasses.GeneralClasses.ChatMessage lastMessage, String title, @Nullable Integer pinOrder, @Nullable SspClasses.VoiceClasses.CallInfo call, Instant timestamp, boolean archived, boolean pinned, int pinnedPriority) {
                super();
                this.id = id;
                this.chatType = chatType;
                this.lastMessage = lastMessage;
                this.title = title;
                this.pinOrder = pinOrder;
                this.call = call;
                this.timestamp = timestamp;
                this.archived = archived;
                this.pinned = pinned;
                this.pinnedPriority = pinnedPriority;
            }
        }

        public enum ChatType {
            direct,
            group,
            text_channel,
        }

        public static class UserAvatarUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.User updated;

            public UserAvatarUpdate(Long userId, SspClasses.GeneralClasses.User updated) {
                super();
                this.userId = userId;
                this.updated = updated;
            }
        }

        public static class UserBannerUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.User updated;

            public UserBannerUpdate(Long userId, SspClasses.GeneralClasses.User updated) {
                super();
                this.userId = userId;
                this.updated = updated;
            }
        }

        public static class UserTagUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.User updated;

            public UserTagUpdate(Long userId, SspClasses.GeneralClasses.User updated) {
                super();
                this.userId = userId;
                this.updated = updated;
            }
        }

        public static class UserBioUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.User updated;

            public UserBioUpdate(Long userId, SspClasses.GeneralClasses.User updated) {
                super();
                this.userId = userId;
                this.updated = updated;
            }
        }

        public static class UserDisplayNameUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.User updated;

            public UserDisplayNameUpdate(Long userId, SspClasses.GeneralClasses.User updated) {
                super();
                this.userId = userId;
                this.updated = updated;
            }
        }

        public static class UserStatusUpdate extends SspClasses.GeneralClasses.SlClass {
            public Long userId;
            public SspClasses.GeneralClasses.UserLastActiveData newStatus;

            public UserStatusUpdate(Long userId, SspClasses.GeneralClasses.UserLastActiveData newStatus) {
                super();
                this.userId = userId;
                this.newStatus = newStatus;
            }
        }

        public static class UserLastActiveData extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.OnlineStatusType type;
            public Instant active;

            public UserLastActiveData(SspClasses.GeneralClasses.OnlineStatusType type, Instant active) {
                super();
                this.type = type;
                this.active = active;
            }
        }

        public enum OnlineStatusType {
            online,
            dnd,
            offline,
        }

        public static class Error extends SspClasses.GeneralClasses.SlClass {
            public int code;
            public String error;

            public Error(int code, String error) {
                super();
                this.code = code;
                this.error = error;
            }
        }

        public static class Nope extends SspClasses.GeneralClasses.SlClass {
        }

        public static class CdnFile extends SspClasses.GeneralClasses.SlClass {
            public String url;

            public CdnFile(String url) {
                super();
                this.url = url;
            }
        }

        public static class UserFileAvatar extends SspClasses.GeneralClasses.UserAvatar {
            public SspClasses.GeneralClasses.CdnFile file;

            public UserFileAvatar(SspClasses.GeneralClasses.CdnFile file) {
                super();
                this.file = file;
            }
        }

        public static class UserAvatar extends SspClasses.GeneralClasses.SlClass {
        }

        public static class LinearGradientOpacity extends SspClasses.GeneralClasses.LinearGradient {
            public int opacity;

            public LinearGradientOpacity(int opacity, String from, String to, int rotation) {
                super(from, to, rotation);
                this.opacity = opacity;
            }
        }

        public static class LinearGradient extends SspClasses.GeneralClasses.SlClass {
            public String from;
            public String to;
            public int rotation;

            public LinearGradient(String from, String to, int rotation) {
                super();
                this.from = from;
                this.to = to;
                this.rotation = rotation;
            }
        }

        public static class UserGradientBanner extends SspClasses.GeneralClasses.UserBanner {
            public SspClasses.GeneralClasses.LinearGradient gradient;

            public UserGradientBanner(SspClasses.GeneralClasses.LinearGradient gradient) {
                super();
                this.gradient = gradient;
            }
        }

        public static class UserImageBanner extends SspClasses.GeneralClasses.UserBanner {
            public SspClasses.GeneralClasses.CdnFile file;
            public SspClasses.GeneralClasses.LinearGradientOpacity gradient;

            public UserImageBanner(SspClasses.GeneralClasses.CdnFile file, SspClasses.GeneralClasses.LinearGradientOpacity gradient) {
                super();
                this.file = file;
                this.gradient = gradient;
            }
        }

        public static abstract class UserBanner extends SspClasses.GeneralClasses.SlClass {
        }

        public static class BoughtUserTag extends SspClasses.GeneralClasses.UserTag {
            public String platform;
            public String platformUrl;
            public float price;
            public SspClasses.ExtraClasses.Currency currency;

            public BoughtUserTag(String platform, String platformUrl, float price, SspClasses.ExtraClasses.Currency currency, String tag) {
                super(tag);
                this.platform = platform;
                this.platformUrl = platformUrl;
                this.price = price;
                this.currency = currency;
            }
        }

        public static class EditableUserTag extends SspClasses.GeneralClasses.UserTag {
            public EditableUserTag(String tag) {
                super(tag);
            }
        }

        public static class UserTag extends SspClasses.GeneralClasses.SlClass {
            public String tag;

            public UserTag(String tag) {
                super();
                this.tag = tag;
            }
        }

        public static class UserTags extends SspClasses.GeneralClasses.SlClass {
            @Nullable
            public SspClasses.GeneralClasses.EditableUserTag editable;
            public List<SspClasses.GeneralClasses.BoughtUserTag> additional;

            public UserTags(@Nullable SspClasses.GeneralClasses.EditableUserTag editable, List<SspClasses.GeneralClasses.BoughtUserTag> additional) {
                super();
                this.editable = editable;
                this.additional = additional;
            }
        }

        public static class InputUserId extends SspClasses.GeneralClasses.UserInput {
            public Long userId;
            public Long accessHash;

            public InputUserId(Long userId, Long accessHash) {
                super();
                this.userId = userId;
                this.accessHash = accessHash;
            }
        }

        public static class InputUserSelf extends SspClasses.GeneralClasses.UserInput {
        }

        public static class InputUserEmpty extends SspClasses.GeneralClasses.UserInput {
        }

        public static class UserInput extends SspClasses.GeneralClasses.SlClass {
        }

        public static class Group extends SspClasses.GeneralClasses.IncludableClass<SspClasses.GeneralClasses.Group> {
            public SspClasses.ExtraClasses.GroupAccessData accessData;
            public int members;
            public boolean large;
            public Long id;
            @Nullable
            public String title;
            @Nullable
            public String description;

            public Group(SspClasses.ExtraClasses.GroupAccessData accessData, int members, boolean large, Long id, @Nullable String title, @Nullable String description) {
                super();
                this.accessData = accessData;
                this.members = members;
                this.large = large;
                this.id = id;
                this.title = title;
                this.description = description;
            }
        }

        public static class GroupMember extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.GeneralClasses.User user;
            public Instant timestamp;

            public GroupMember(SspClasses.GeneralClasses.User user, Instant timestamp) {
                super();
                this.user = user;
                this.timestamp = timestamp;
            }
        }

        public static class User extends SspClasses.GeneralClasses.IncludableClass<SspClasses.GeneralClasses.User> {
            public Long accessHash;
            public SspClasses.GeneralClasses.UserLastActiveData activity;
            @Nullable
            public SspClasses.GeneralClasses.UserAvatar avatar;
            public SspClasses.GeneralClasses.UserBanner banner;
            public SspClasses.GeneralClasses.UserTags tags;
            public Long id;
            public String displayName;
            @Nullable
            public String bio;
            public Instant createdAt;

            public User(Long accessHash, SspClasses.GeneralClasses.UserLastActiveData activity, @Nullable SspClasses.GeneralClasses.UserAvatar avatar, SspClasses.GeneralClasses.UserBanner banner, SspClasses.GeneralClasses.UserTags tags, Long id, String displayName, @Nullable String bio, Instant createdAt) {
                super();
                this.accessHash = accessHash;
                this.activity = activity;
                this.avatar = avatar;
                this.banner = banner;
                this.tags = tags;
                this.id = id;
                this.displayName = displayName;
                this.bio = bio;
                this.createdAt = createdAt;
            }
        }

        public static abstract class IncludableClass<T> extends SspClasses.GeneralClasses.SlClass {
        }

        public enum MediaType {
            file,
            voice_audio,
            voice_video,
            photo,
            video,
            audio,
            gif,
        }

        public static class Auth {

            public static class RegisterDataInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String tag;
                public String displayName;
                @Nullable
                public SspClasses.TransferredFile avatar;
                public String password;

                public RegisterDataInput(String tag, String displayName, @Nullable SspClasses.TransferredFile avatar, String password) {
                    super();
                    this.tag = tag;
                    this.displayName = displayName;
                    this.avatar = avatar;
                    this.password = password;
                }
            }

            public static class TOTPInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String code;

                public TOTPInput(String code) {
                    super();
                    this.code = code;
                }
            }

            public static class CBTCodeInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String code;

                public CBTCodeInput(String code) {
                    super();
                    this.code = code;
                }
            }

            public static class EmailCodeInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String code;

                public EmailCodeInput(String code) {
                    super();
                    this.code = code;
                }
            }

            public static class EmailInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String email;

                public EmailInput(String email) {
                    super();
                    this.email = email;
                }
            }

            public static class TokenInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String token;

                public TokenInput(String token) {
                    super();
                    this.token = token;
                }
            }

            public static class PasswordInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public String password;

                public PasswordInput(String password) {
                    super();
                    this.password = password;
                }
            }

            public static class AuthTypeInput extends SspClasses.GeneralClasses.Auth.AuthInputClass {
                public SspClasses.GeneralClasses.Auth.AuthType type;
                public String device;

                public AuthTypeInput(SspClasses.GeneralClasses.Auth.AuthType type, String device) {
                    super();
                    this.type = type;
                    this.device = device;
                }
            }

            public static class AuthorizationError extends SspClasses.GeneralClasses.Auth.AuthState {
                public SspClasses.GeneralClasses.Auth.AuthError type;

                public AuthorizationError(SspClasses.GeneralClasses.Auth.AuthError type) {
                    super();
                    this.type = type;
                }
            }

            public static class SuccessfullyAuthorization extends SspClasses.GeneralClasses.Auth.AuthState {
                public String token;

                public SuccessfullyAuthorization(String token) {
                    super();
                    this.token = token;
                }
            }

            public static class WaitingForToken extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForRegisterData extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForCBTCode extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForEmailCode extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForEmail extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForTOTP extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForPassword extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public static class WaitingForAuthType extends SspClasses.GeneralClasses.Auth.AuthState {
            }

            public enum AuthType {
                TOKEN,
                EMAIL,
            }

            public enum AuthError {
                INTERNAL_INVALID_AUTH_CLASS,
                CBT_CODE_INVALID,
                CBT_CODE_REGISTERED_BY_OTHER,
                ACCOUNT_NOT_FOUND,
                ACCOUNT_BANNED,
                INVALID_TOKEN,
                INVALID_CODE,
                INVALID_PASSWORD,
                INVALID_REGISTER_DATA,
                EMAIL_INVALID,
                EMAIL_EXISTS,
                USERNAME_EXISTS,
            }

            public static abstract class AuthInputClass extends SspClasses.GeneralClasses.SlClass {
            }

            public static abstract class AuthState extends SspClasses.GeneralClasses.SlClass {
            }
        }

        public static abstract class SlClass {
        }
    }

    public static class SettingsClasses {

        public static class Privacy {

            public static class SettingValue extends SspClasses.GeneralClasses.SlClass {
            }

            public static class PrivacySettings extends SspClasses.GeneralClasses.SlClass {
            }
        }

        public static class Sessions {

            public static class Session extends SspClasses.GeneralClasses.IncludableClass<SspClasses.SettingsClasses.Sessions.Session> {
                public SspClasses.SettingsClasses.Sessions.DeviceType deviceType;
                public Long id;
                public String device;
                public Instant timestamp;
                public Instant active;

                public Session(SspClasses.SettingsClasses.Sessions.DeviceType deviceType, Long id, String device, Instant timestamp, Instant active) {
                    super();
                    this.deviceType = deviceType;
                    this.id = id;
                    this.device = device;
                    this.timestamp = timestamp;
                    this.active = active;
                }
            }

            public enum DeviceType {
                WEB,
                DESKTOP,
                ANDROID,
                IOS,
            }
        }
    }

    public static class VoiceClasses {

        public static class CallInfo extends SspClasses.GeneralClasses.SlClass {
            public SspClasses.VoiceClasses.VoiceServerLocation voiceServer;
            public int members;
            public Instant timestamp;

            public CallInfo(SspClasses.VoiceClasses.VoiceServerLocation voiceServer, int members, Instant timestamp) {
                super();
                this.voiceServer = voiceServer;
                this.members = members;
                this.timestamp = timestamp;
            }
        }

        public static class VoiceChannelData extends SspClasses.GeneralClasses.SlClass {
            public String endpoint;
            public String token;
            public String name;

            public VoiceChannelData(String endpoint, String token, String name) {
                super();
                this.endpoint = endpoint;
                this.token = token;
                this.name = name;
            }
        }

        public static class VoiceServerLocation extends SspClasses.GeneralClasses.SlClass {
            public String serverId;
            public String country;
            public String region;

            public VoiceServerLocation(String serverId, String country, String region) {
                super();
                this.serverId = serverId;
                this.country = country;
                this.region = region;
            }
        }
    }
}