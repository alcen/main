package csdev.couponstash.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import csdev.couponstash.commons.core.Messages;
import csdev.couponstash.commons.core.index.Index;
import csdev.couponstash.logic.commands.exceptions.CommandException;
import csdev.couponstash.model.Model;
import csdev.couponstash.model.coupon.Coupon;

/**
 * Share a Coupon in the CouponStash.
 */
public class ShareCommand extends IndexedCommand {

    public static final String COMMAND_WORD = "share";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shares the coupon identified by the "
            + "index number with the world! Coupon will be rendered and saved as an image file "
            + "for easy sharing.\n\n"
            + "Parameters: INDEX (must be a positive integer)\n\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SHARE_COUPON_SUCCESS = "Coupon successfully saved to: %s";

    public static final String MESSAGE_SHARE = "Attempting to share coupon: %s";

    public static final String MESSAGE_WRITE_TO_IMAGE_ERROR =
            "Problem encountered while saving to image. Please try sharing again!";

    public static final String MESSAGE_DIALOG_CLOSED =
            "Coupon was not saved, save dialog was closed without choosing a directory.";

    public static final String FORMAT = "png";

    /**
     * @param index of the coupon in the filtered coupon list to share
     */
    public ShareCommand(Index index) {
        super(index);
    }

    @Override
    public CommandResult execute(Model model, String commandText) throws CommandException {
        requireNonNull(model);
        List<Coupon> lastShownList = model.getFilteredCouponList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_COUPON_DISPLAYED_INDEX);
        }

        Coupon couponToShare = lastShownList.get(targetIndex.getZeroBased());

        return new CommandResult(
                String.format(MESSAGE_SHARE, couponToShare.getName()),
                Optional.empty(),
                Optional.of(couponToShare),
                false,
                false
        );
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ShareCommand
                && targetIndex.equals(((ShareCommand) other).targetIndex));
    }
}
